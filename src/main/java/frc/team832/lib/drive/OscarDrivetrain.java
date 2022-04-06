package frc.team832.lib.drive;

import java.util.List;

import com.ctre.phoenix.sensors.BasePigeon;
import com.ctre.phoenix.sensors.Pigeon2;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.ctre.phoenix.sensors.WPI_Pigeon2;
import com.ctre.phoenix.sensors.WPI_PigeonIMU;


import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.OscarRamseteCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.motorcontrol.SmartMCSim;
import frc.team832.lib.motors.WheeledPowerTrain;

// TODO: make flexible enough for externally-encodered drivetrains

public class OscarDrivetrain {
	private static final String DB_TABNAME = OscarDrivetrain.class.getSimpleName();

	private final SmartMC<?> m_leftMotor, m_rightMotor;
	private final Gyro m_gyro;
	private final WheeledPowerTrain m_powertrain;
	private final OscarDiffDrive m_diffDrive;
	private final DifferentialDriveOdometry m_odometry;
	private final DifferentialDriveKinematics m_kinematics;
	private final SimpleMotorFeedforward m_leftFeedforward, m_rightFeedforward;

	private final PIDController m_leftPIDController, m_rightPIDController;
	private final RamseteController m_ramseteController = new RamseteController();
	private final Field2d m_field = new Field2d();

	private Pose2d m_pose = new Pose2d();

	private final DifferentialDrivetrainSim m_driveSim;
	private final SmartMCSim m_leftMotorSim, m_rightMotorSim;

	private Rotation2d m_lastGyroYaw = Rotation2d.fromDegrees(0);
	
	// Motor data
	private final NetworkTableEntry nte_leftMotorDutyCycle, nte_rightMotorDutyCycle,
		nte_leftMotorOutputVoltage, nte_rightMotorOutputVoltage,
		nte_leftMotorInputCurrent, nte_rightMotorInputCurrent;

	// Sensor data
	private final NetworkTableEntry nte_gyroYaw,
		nte_leftEncoderRaw, nte_rightEncoderRaw, 
		nte_leftEncoderMeters, nte_rightEncoderMeters,
		nte_leftEncoderMetersPerSec, nte_rightEncoderMetersPerSec;

	// Odometry data
	private final NetworkTableEntry nte_robotPoseX, nte_robotPoseY, nte_robotPoseRot;

	public OscarDrivetrain(SmartMC<?> leftMotor, SmartMC<?> rightMotor, Gyro gyro, OscarDTCharacteristics dtCharacteristics) {
		m_leftMotor = leftMotor;
		m_rightMotor = rightMotor;
		m_gyro = gyro;
		m_powertrain = dtCharacteristics.powertrain;
		
		m_diffDrive = new OscarDiffDrive(
			leftMotor, rightMotor
			// uncomment for feedforward openloop teleop control
			// dtCharacteristics.leftFeedforward, 
			// dtCharacteristics.rightFeedforward,
			// 3.0 // meters per sec
		);
			
		m_odometry = new DifferentialDriveOdometry(getGyroHeading());
		m_kinematics = new DifferentialDriveKinematics(dtCharacteristics.trackwidthMeters);
		
		m_leftPIDController = new PIDController(dtCharacteristics.leftkP, 0, 0);
		m_rightPIDController = new PIDController(dtCharacteristics.rightkP, 0, 0);

		m_leftFeedforward = dtCharacteristics.leftFeedforward;
		m_rightFeedforward = dtCharacteristics.rightFeedforward;
		
		var drivePlant = LinearSystemId.createDrivetrainVelocitySystem(
			dtCharacteristics.powertrain.getWPILibPlantMotor(), 
			dtCharacteristics.massKg, 
			dtCharacteristics.powertrain.wheelDiameterMeters / 2, 
			dtCharacteristics.trackwidthMeters, 
			dtCharacteristics.moiKgM2,
			dtCharacteristics.powertrain.gearbox.totalReduction
		);

		m_driveSim = new DifferentialDrivetrainSim(
			drivePlant,
			dtCharacteristics.powertrain.getWPILibPlantMotor(),
			dtCharacteristics.powertrain.gearbox.totalReduction,
			dtCharacteristics.trackwidthMeters,
			dtCharacteristics.powertrain.wheelDiameterMeters / 2.0,
		null);

		// m_driveSim = new DifferentialDrivetrainSim(
		// 	dtCharacteristics.powertrain.getWPILibPlantMotor(), // motors
		// 	dtCharacteristics.powertrain.gearbox.totalReduction, // gearbox reduction
		// 	dtCharacteristics.moiKgM2, // MoI (kg / m^2) from CAD
		// 	dtCharacteristics.massKg, // Mass (kg), from competition weight
		// 	dtCharacteristics.powertrain.wheelDiameterMeters / 2, // wheel radius (meters)
		// 	Units.inchesToMeters(dtCharacteristics.trackwidthMeters), // robot track width (meters)
			
		// 	// The standard deviations for measurement noise:
		// 	// x and y:          0.001 m
		// 	// heading:          0.001 rad
		// 	// l and r velocity: 0.1   m/s
		// 	// l and r position: 0.005 m
		// 	null
		// 	// VecBuilder.fill(0.001, 0.001, 0.001, 0.1, 0.1, 0.005, 0.005)
		// );

		m_leftMotorSim = m_leftMotor.getSim();
		m_rightMotorSim = m_rightMotor.getSim();

		// m_field.setRobotPose(new Pose2d());

		// Telemetry setup
		DashboardManager.addTab(DB_TABNAME);
		
		// Motor data
		nte_leftMotorDutyCycle = DashboardManager.addTabNumberBar(DB_TABNAME, "Left DutyCycle", -1.0, 1.0);
		nte_rightMotorDutyCycle = DashboardManager.addTabNumberBar(DB_TABNAME, "Right DutyCycle", -1.0, 1.0);

		nte_leftMotorOutputVoltage = DashboardManager.addTabNumberBar(DB_TABNAME, "Left Out Voltage", -13.0, 13.0);
		nte_rightMotorOutputVoltage = DashboardManager.addTabNumberBar(DB_TABNAME, "Right Out Voltage", -13.0, 13.0);

		nte_leftMotorInputCurrent = DashboardManager.addTabNumberBar(DB_TABNAME, "Left Input Amps", -100.0, 100.0);
		nte_rightMotorInputCurrent = DashboardManager.addTabNumberBar(DB_TABNAME, "Right Input Amps", -100.0, 100.0);

		// Sensor data
		nte_gyroYaw = DashboardManager.addTabItem(DB_TABNAME, "Gyro Yaw", 0.0, BuiltInWidgets.kGyro);

		nte_leftEncoderRaw = DashboardManager.addTabItem(DB_TABNAME, "Left Enc Raw", 0.0);
		nte_rightEncoderRaw = DashboardManager.addTabItem(DB_TABNAME, "Right Enc Raw", 0.0);

		nte_leftEncoderMeters = DashboardManager.addTabItem(DB_TABNAME, "Left Enc Meters", 0.0);
		nte_rightEncoderMeters = DashboardManager.addTabItem(DB_TABNAME, "Right Enc Meters", 0.0);

		nte_leftEncoderMetersPerSec = DashboardManager.addTabNumberBar(DB_TABNAME, "Left Enc MpS", -5.0, 5.0);
		nte_rightEncoderMetersPerSec = DashboardManager.addTabNumberBar(DB_TABNAME, "Right Enc MpS", -5.0, 5.0);

		// Odometry data
		nte_robotPoseX = DashboardManager.addTabItem(DB_TABNAME, "Robot Pose/X", 0.0);
		nte_robotPoseY = DashboardManager.addTabItem(DB_TABNAME, "Robot Pose/Y", 0.0);
		nte_robotPoseRot = DashboardManager.addTabItem(DB_TABNAME, "Robot Pose/Rotation", 0.0);
	}
			
	/**
	 * Get differential drive controller for tele-op control.
	 * @return {@link frc.team832.lib.drive.OscarDiffDrive} 
	 */
	public OscarDiffDrive getDiffDrive() {
		return m_diffDrive;
	}
	
	/**
	 * Get gyro heading (yaw)
	 * @return {@link edu.wpi.first.math.geometry.Rotation2d} of the heading.
	 */
	public Rotation2d getGyroHeading() {
		return m_gyro.getRotation2d();
	}
	
	/**
	 * Get drivetrain pose
	 * @return {@link edu.wpi.first.math.geometry.Pose2d} of the robot drivetrain.
	 */
	public Pose2d getPose() {
		return m_pose;
	}
	
	/**
	 * Reset drivetrain pose to 0, 0
	 */
	public void resetPose() {
		resetPose(new Pose2d());
	}

	/**
	 * Reset drivetrain pose
	 * @param newPose {@link edu.wpi.first.math.geometry.Pose2d} of the robot drivetrain.
	 */
	public void resetPose(Pose2d newPose) {
		m_leftMotor.rezeroSensor();
		m_rightMotor.rezeroSensor();

		if (RobotBase.isSimulation()) {
			m_leftMotorSim.setSensorPosition(0);
			m_rightMotorSim.setSensorPosition(0);
			// ((BasePigeon)m_gyro).getSimCollection().setRawHeading(newPose.getRotation().getDegrees());
			m_driveSim.setPose(newPose);
			m_driveSim.update(0.02);
		}

		// ((WPI_Pigeon2)m_gyro).setYaw(newPose.getRotation().getDegrees());

		m_odometry.resetPosition(newPose, getGyroHeading());
		m_pose = newPose;
	}

	public void setMotorVoltages(double left, double right) {
		m_leftMotor.setVoltage(left);
		m_rightMotor.setVoltage(right);
	}

	public double getLeftWheelMeters() {
		var rots = m_leftMotor.getSensorPosition();
		var meters = m_powertrain.calcWheelDistanceMeters(rots);
		return meters;
	}

	public double getRightWheelMeters() {
		var rots = m_rightMotor.getSensorPosition();
		var meters = m_powertrain.calcWheelDistanceMeters(rots);
		return meters;
	}

	public double getLeftWheelMetersPerSec() {
		var veloRpm = m_leftMotor.getSensorVelocity();
		var mps = m_powertrain.calcMetersPerSec(veloRpm);
		return mps;
	}

	public double getRightWheelMetersPerSec() {
		var veloRpm = m_rightMotor.getSensorVelocity();
		var mps = m_powertrain.calcMetersPerSec(veloRpm);
		return mps;
	}

	public DifferentialDriveWheelSpeeds getWheelSpeeds() {
		return new DifferentialDriveWheelSpeeds(
			getLeftWheelMetersPerSec(), getRightWheelMetersPerSec()
		);
	}

	public CommandBase generateRamseteCommand(Trajectory path, SubsystemBase drivetrainSubsystem) {
		return generateRamseteCommand(path, drivetrainSubsystem, true);
	}

	public CommandBase generateRamseteCommand(Trajectory path, SubsystemBase drivetrainSubsystem, boolean showPath) {
		var showOnFieldCommand = new InstantCommand(() -> {
			if (showPath) {
				m_field.getObject("RamseteCommandTraj").setTrajectory(path);
			} else {
				m_field.getObject("RamseteCommandTraj").setPoses(List.of());
			}
		}, drivetrainSubsystem);

		// var resetPoseCommand = new InstantCommand(() -> {
		// 	resetPose(path.getInitialPose());
		// }, drivetrainSubsystem).withTimeout(0.125);

		var noPid = new PIDController(0, 0, 0);

		var ramseteCommand = new OscarRamseteCommand(
			path, this::getPose, m_ramseteController, 
			m_leftFeedforward, m_rightFeedforward, 
			m_kinematics, 
			this::getWheelSpeeds, 
			noPid, noPid,
			// m_leftPIDController, m_rightPIDController,
			this::setMotorVoltages,
			drivetrainSubsystem
		);

		return showOnFieldCommand.andThen(ramseteCommand);
		// return showOnFieldCommand.andThen(resetPoseCommand).andThen(ramseteCommand);
	}
	
	public void simulationPeriodic() {
		if (!RobotState.isEnabled()) return;

		m_leftMotorSim.setBusVoltage(RobotController.getBatteryVoltage());
		m_rightMotorSim.setBusVoltage(RobotController.getBatteryVoltage());

		m_driveSim.setInputs(
			m_leftMotorSim.getOutputVoltage(),
			-m_rightMotorSim.getOutputVoltage()
		);

		m_driveSim.update(0.02);

		var leftMeters = m_driveSim.getLeftPositionMeters();
		var leftMps = m_driveSim.getLeftVelocityMetersPerSecond();
		SmartDashboard.putNumber("Sim/LeftMeters", leftMeters);
		SmartDashboard.putNumber("Sim/LeftMps", leftMps);

		var leftSensorPosition = m_powertrain.calcEncoderRotationsFromMeters(leftMeters);
		var leftSensorVelocity = m_powertrain.calcEncoderRpmFromMetersPerSec(leftMps);
		m_leftMotorSim.setSensorPosition(leftSensorPosition);
		m_leftMotorSim.setSensorVelocity(leftSensorVelocity);

		var rightMeters = -m_driveSim.getRightPositionMeters();
		var rightMps = -m_driveSim.getRightVelocityMetersPerSecond();
		SmartDashboard.putNumber("Sim/RightMeters", rightMeters);
		SmartDashboard.putNumber("Sim/RightMps", rightMps);

		var rightSensorPosition = m_powertrain.calcEncoderRotationsFromMeters(rightMeters);
		var rightSensorVelocity = m_powertrain.calcEncoderRpmFromMetersPerSec(rightMps);
		m_rightMotorSim.setSensorPosition(rightSensorPosition);
		m_rightMotorSim.setSensorVelocity(rightSensorVelocity);

		((BasePigeon)m_gyro).getSimCollection().setRawHeading(m_driveSim.getHeading().getDegrees());
	}

	/**
	 * Call this in a high-frequency loop to update drivetrain telemetry and odometry.
	 */
	public void periodic() {
		// values that differ between sim and real
		double leftRotations = 0, rightRotations = 0;
		double leftMeters = 0, rightMeters = 0;
		double leftMetersPerSec = 0, rightMetersPerSec = 0;
		double leftAmps = 0, rightAmps = 0;

		boolean ran = false;

		// the same between sim/real
		double leftVolts = m_leftMotor.getOutputVoltage();
		double rightVolts = m_rightMotor.getOutputVoltage();

		// if (RobotBase.isReal()) {
			leftRotations = m_leftMotor.getSensorPosition();
			rightRotations = m_rightMotor.getSensorPosition();
			leftMeters = getLeftWheelMeters();
			rightMeters = getRightWheelMeters();
			leftMetersPerSec = getLeftWheelMetersPerSec();
			rightMetersPerSec = getRightWheelMetersPerSec();
			leftAmps = m_leftMotor.getInputCurrent();
			rightAmps = m_leftMotor.getInputCurrent();

			m_lastGyroYaw = m_gyro.getRotation2d();
			
			// Thank you CTRE.
			// boolean gyroIsPigeon1 = m_gyro instanceof PigeonIMU;
			// if (RobotBase.isReal() && gyroIsPigeon1) {
				// m_lastGyroYaw = m_lastGyroYaw.times(-1);
			// }

			// Update pose
			m_pose = m_odometry.update(m_lastGyroYaw, leftMeters, rightMeters);
			// if (RobotBase.isReal()) {
				// m_pose = new Pose2d(-m_pose.getX(), m_pose.getY(), m_pose.getRotation());
			// }
			ran = true;
		// } else {
		// 	// run drive simulation
		// 	if (DriverStation.isEnabled()) {
		// 		m_driveSim.setInputs(leftVolts, rightVolts);
		// 		m_driveSim.update(0.02);

		// 			// m_pose = m_driveSim.getPose();
		// 		m_lastGyroYaw = m_driveSim.getHeading();
		// 		// TODO: fix cast hack
		// 		((BasePigeon)m_gyro).getSimCollection().setRawHeading(m_lastGyroYaw.getDegrees());

		// 		leftMeters = m_driveSim.getLeftPositionMeters();
		// 		rightMeters = m_driveSim.getRightPositionMeters();

		// 		m_pose = m_odometry.update(m_lastGyroYaw, leftMeters, rightMeters);

		// 		leftRotations = m_powertrain.calcEncoderRotationsFromMeters(m_driveSim.getLeftPositionMeters());
		// 		rightRotations = m_powertrain.calcEncoderRotationsFromMeters(m_driveSim.getRightPositionMeters());

		// 		leftMetersPerSec = m_powertrain.calcMetersPerSec(m_leftMotor.getSensorVelocity());

		// 		// leftMetersPerSec = m_driveSim.getLeftVelocityMetersPerSecond();
		// 		// rightMetersPerSec = m_driveSim.getRightVelocityMetersPerSecond();

		// 		leftAmps = m_driveSim.getLeftCurrentDrawAmps();
		// 		rightAmps = m_driveSim.getRightCurrentDrawAmps();

		// 		m_leftMotorSim.setSensorPosition(leftRotations);
		// 		m_rightMotorSim.setSensorPosition(rightRotations);
		// 		ran = true;
		// 	}
		// }

		nte_gyroYaw.setDouble(m_lastGyroYaw.getDegrees());

		nte_leftMotorDutyCycle.setDouble(m_leftMotor.get());
		nte_rightMotorDutyCycle.setDouble(m_rightMotor.get());

		nte_leftMotorOutputVoltage.setDouble(leftVolts);
		nte_rightMotorOutputVoltage.setDouble(rightVolts);

		// if (ran) {
			nte_leftEncoderRaw.setDouble(leftRotations);
			nte_rightEncoderRaw.setDouble(rightRotations);
	
			nte_leftEncoderMeters.setDouble(leftMeters);
			nte_rightEncoderMeters.setDouble(rightMeters);
	
			nte_leftEncoderMetersPerSec.setDouble(leftMetersPerSec);
			nte_rightEncoderMetersPerSec.setDouble(rightMetersPerSec);
	
			nte_leftMotorInputCurrent.setDouble(leftAmps);
			nte_rightMotorInputCurrent.setDouble(rightAmps);
		// }

		
		nte_robotPoseX.setDouble(m_pose.getX());
		nte_robotPoseY.setDouble(m_pose.getY());
		nte_robotPoseRot.setDouble(m_pose.getRotation().getDegrees());
		
		m_field.setRobotPose(m_pose);
		SmartDashboard.putData("Oscar Drivetrain/Field", m_field);
	}

	/**
	 * Stop the drivetrain.
	 */
	public void stop() {
		m_diffDrive.stopMotor();
	}

	public void addPoseToField(Pose2d pose, String name) {
		var obj = m_field.getObject(name);
		obj.setPose(pose);
	}

	public void addTrajectoryToField(Trajectory traj, String name) {
		var obj = m_field.getObject(name);
		obj.setTrajectory(traj);
	}

	// public void addRobotTrajectoryToField(Trajectory traj) {
		// m_field.getRobotObject().setTrajectory(traj);
	// }
}
