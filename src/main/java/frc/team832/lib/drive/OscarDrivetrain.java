package frc.team832.lib.drive;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.motors.WheeledPowerTrain;

public class OscarDrivetrain {
	private final SmartMC<?, ?> m_leftMotor, m_rightMotor;
	private final Gyro m_gyro;
	private final WheeledPowerTrain m_powertrain;
	private final OscarDiffDrive m_diffDrive;
	private final DifferentialDriveOdometry m_odometry;
	private final DifferentialDriveKinematics m_kinematics;

	private final PIDController m_leftPIDController, m_rightPIDController;
	private final RamseteController m_ramseteController = new RamseteController();

	private Pose2d m_pose;
	private Field2d m_field;

	private final DifferentialDrivetrainSim m_driveSim;

	// TODO: NTEs for all debug variables
	// private NetworkTableEntry
	
	public OscarDrivetrain(SmartMC<?, ?> leftMotor, SmartMC<?, ?> rightMotor, Gyro gyro, OscarDTCharacteristics dtCharacteristics) {
		m_leftMotor = leftMotor;
		m_rightMotor = rightMotor;
		m_gyro = gyro;
		m_powertrain = dtCharacteristics.powertrain;
		
		m_diffDrive = new OscarDiffDrive(
			leftMotor, rightMotor, 
			dtCharacteristics.leftFeedforward, 
			dtCharacteristics.rightFeedforward,
			dtCharacteristics.wheelbaseInches);

		m_odometry = new DifferentialDriveOdometry(getGyroHeading());
		m_kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(dtCharacteristics.wheelbaseInches));

		m_leftPIDController = new PIDController(dtCharacteristics.leftkP, 0, 0);
		m_rightPIDController = new PIDController(dtCharacteristics.rightkP, 0, 0);

		// TODO: fix!
		var simMotor = DCMotor.getFalcon500(dtCharacteristics.powertrain.motorCount);

		m_driveSim = new DifferentialDrivetrainSim(
			simMotor, // motors
			dtCharacteristics.powertrain.gearbox.getTotalReduction(), // gearbox reduction
			5.120993184, // MoI (kg / m^2) from CAD
			Units.lbsToKilograms(118.9), // Mass (kg), from competition weight
			dtCharacteristics.powertrain.wheelDiameterMeters / 2, // wheel radius (meters)
			Units.inchesToMeters(dtCharacteristics.wheelbaseInches), // robot track width (meters)

			// The standard deviations for measurement noise:
			// x and y:          0.001 m
			// heading:          0.001 rad
			// l and r velocity: 0.1   m/s
			// l and r position: 0.005 m
			VecBuilder.fill(0.001, 0.001, 0.001, 0.1, 0.1, 0.005, 0.005)
		);
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
	 * Call this in a high-frequency loop to update drivetrain odometry.
	 */
	public void periodic() {
		var gyroAngle = getGyroHeading();
	
		double leftRotations, rightRotations;

		if (RobotBase.isReal()) {
			leftRotations = m_leftMotor.getSensorPosition();
			rightRotations = m_rightMotor.getSensorPosition();
		} else {
			leftRotations
		}

		var leftMeters = m_powertrain.calculateWheelDistanceMeters(leftRotations);
		var rightMeters = m_powertrain.calculateWheelDistanceMeters(rightRotations);
		
		m_pose = m_odometry.update(gyroAngle, leftMeters, rightMeters);

		
		// TODO: investigate why this causes NPE
		// m_field.setRobotPose(m_pose);
	}
}
