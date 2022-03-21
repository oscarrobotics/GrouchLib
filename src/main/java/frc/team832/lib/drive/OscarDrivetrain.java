package frc.team832.lib.drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.motorcontrol.SmartMCSimCollection;
import frc.team832.lib.motors.WheeledPowerTrain;

public class OscarDrivetrain {
	private static final String DB_TABNAME = OscarDrivetrain.class.getSimpleName();

	private final SmartMC<?, ?> m_leftMotor, m_rightMotor;
	private final Gyro m_gyro;
	private final WheeledPowerTrain m_powertrain;
	private final OscarDiffDrive m_diffDrive;
	private final DifferentialDriveOdometry m_odometry;
	private final DifferentialDriveKinematics m_kinematics;

	private final PIDController m_leftPIDController, m_rightPIDController;
	private final RamseteController m_ramseteController = new RamseteController();
	private final Field2d m_field = new Field2d();

	private Pose2d m_pose = new Pose2d();

	private final DifferentialDrivetrainSim m_driveSim;
	private final SmartMCSimCollection m_leftSimCollection, m_rightSimCollection;

	// TODO: NTEs for all debug variables

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

	public OscarDrivetrain(SmartMC<?, ?> leftMotor, SmartMC<?, ?> rightMotor, Gyro gyro, OscarDTCharacteristics dtCharacteristics) {
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
		m_kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(dtCharacteristics.wheelbaseMeters));
		
		m_leftPIDController = new PIDController(dtCharacteristics.leftkP, 0, 0);
		m_rightPIDController = new PIDController(dtCharacteristics.rightkP, 0, 0);
		
		
		m_driveSim = new DifferentialDrivetrainSim(
			dtCharacteristics.powertrain.getWPILibPlantMotor(), // motors
			dtCharacteristics.powertrain.gearbox.totalReduction, // gearbox reduction
			dtCharacteristics.moiKgM2, // MoI (kg / m^2) from CAD
			dtCharacteristics.massKg, // Mass (kg), from competition weight
			dtCharacteristics.powertrain.wheelDiameterMeters / 2, // wheel radius (meters)
			Units.inchesToMeters(dtCharacteristics.wheelbaseMeters), // robot track width (meters)
			
			// The standard deviations for measurement noise:
			// x and y:          0.001 m
			// heading:          0.001 rad
			// l and r velocity: 0.1   m/s
			// l and r position: 0.005 m
			null
			// VecBuilder.fill(0.001, 0.001, 0.001, 0.1, 0.1, 0.005, 0.005)
		);

		m_leftSimCollection = m_leftMotor.getSimCollection();
		m_rightSimCollection = m_rightMotor.getSimCollection();

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
	 * Reset drivetrain pose.
	 */
	public void resetPose() {
		m_odometry.resetPosition(new Pose2d(), getGyroHeading());
	}
	
	/**
	 * Call this in a high-frequency loop to update drivetrain telemetry and odometry.
	 */
	public void periodic() {
		// values that differ between sim and real
		Rotation2d gyroYaw;
		double leftRotations, rightRotations;
		double leftMeters, rightMeters;
		double leftMetersPerSec, rightMetersPerSec;
		double leftAmps, rightAmps;

		// the same between sim/real
		double leftVolts = m_leftMotor.getOutputVoltage();
		double rightVolts = m_rightMotor.getOutputVoltage();

		if (RobotBase.isReal()) {
			leftRotations = m_leftMotor.getSensorPosition();
			rightRotations = m_rightMotor.getSensorPosition();
			leftMeters = m_powertrain.calcWheelDistanceMeters(leftRotations);
			rightMeters = m_powertrain.calcWheelDistanceMeters(rightRotations);
			leftMetersPerSec = m_powertrain.calcMetersPerSec(m_leftMotor.getSensorVelocity());
			rightMetersPerSec = m_powertrain.calcMetersPerSec(m_rightMotor.getSensorVelocity());
			leftAmps = m_leftMotor.getInputCurrent();
			rightAmps = m_leftMotor.getInputCurrent();

			gyroYaw = m_gyro.getRotation2d();
			
			// Update pose
			m_pose = m_odometry.update(gyroYaw, leftMeters, rightMeters);
		} else {
			// run drive simulation
			if (DriverStation.isEnabled()) {
				m_driveSim.setInputs(leftVolts, rightVolts);
			m_driveSim.update(0.02);
			}

			leftMeters = m_driveSim.getLeftPositionMeters();
			rightMeters = m_driveSim.getRightPositionMeters();

			leftRotations = m_powertrain.calcEncoderRotationsFromMeters(m_driveSim.getLeftPositionMeters());
			rightRotations = m_powertrain.calcEncoderRotationsFromMeters(m_driveSim.getRightPositionMeters());

			leftMetersPerSec = m_driveSim.getLeftVelocityMetersPerSecond();
			rightMetersPerSec = m_driveSim.getRightVelocityMetersPerSecond();

			leftAmps = m_driveSim.getLeftCurrentDrawAmps();
			rightAmps = m_driveSim.getRightCurrentDrawAmps();

			m_leftSimCollection.setSensorPosition(leftRotations);
			m_rightSimCollection.setSensorPosition(rightRotations);

			m_pose = m_driveSim.getPose();
			gyroYaw = m_driveSim.getHeading();
		}

		nte_gyroYaw.setDouble(gyroYaw.getDegrees());

		nte_leftMotorDutyCycle.setDouble(m_leftMotor.get());
		nte_rightMotorDutyCycle.setDouble(m_rightMotor.get());

		nte_leftMotorOutputVoltage.setDouble(leftVolts);
		nte_rightMotorOutputVoltage.setDouble(rightVolts);

		nte_leftEncoderRaw.setDouble(leftRotations);
		nte_rightEncoderRaw.setDouble(rightRotations);

		nte_leftEncoderMeters.setDouble(leftMeters);
		nte_rightEncoderMeters.setDouble(rightMeters);

		nte_leftEncoderMetersPerSec.setDouble(leftMetersPerSec);
		nte_rightEncoderMetersPerSec.setDouble(rightMetersPerSec);

		nte_leftMotorInputCurrent.setDouble(leftAmps);
		nte_rightMotorInputCurrent.setDouble(rightAmps);
		
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
}
