package frc.team832.lib.drive;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.motors.WheeledPowerTrain;

public class OscarDrivetrain {
	private final SmartMC<?> m_leftMotor, m_rightMotor;
	private final Gyro m_gyro;
	private final WheeledPowerTrain m_powertrain;
	private final OscarDiffDrive m_diffDrive;
	private final DifferentialDriveOdometry m_odometry;
	private final DifferentialDriveKinematics m_kinematics;
	private final SimpleMotorFeedforward m_leftFF, m_rightFF;

	private Pose2d m_pose;
	
	// TODO: NTEs for all debug variables
	// private NetworkTableEntry
	
	public OscarDrivetrain(
		SmartMC<?> leftMotor, SmartMC<?> rightMotor, 
		SimpleMotorFeedforward leftFeedforward, 
		SimpleMotorFeedforward rightFeedforward, 
		Gyro gyro, WheeledPowerTrain dtPowertrain,
		double wheelbaseInches) {
		m_leftMotor = leftMotor;
		m_rightMotor = rightMotor;
		m_leftFF = leftFeedforward;
		m_rightFF = rightFeedforward;
		m_gyro = gyro;
		m_powertrain = dtPowertrain;
		
		m_diffDrive = new OscarDiffDrive(leftMotor, rightMotor, leftFeedforward, rightFeedforward);
		m_odometry = new DifferentialDriveOdometry(getGyroHeading());
		m_kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(wheelbaseInches));
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
	
		var leftRotations = m_leftMotor.getSensorPosition();
		var rightRotations = m_rightMotor.getSensorPosition();
		var leftMeters = m_powertrain.calculateWheelDistanceMeters(leftRotations);
		var rightMeters = m_powertrain.calculateWheelDistanceMeters(rightRotations);
		
		m_pose = m_odometry.update(gyroAngle, leftMeters, rightMeters);
	}
}
