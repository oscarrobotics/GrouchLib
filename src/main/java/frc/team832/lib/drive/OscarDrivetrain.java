package frc.team832.lib.drive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
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
	
	// TODO: NTEs for all debug variables
	// private NetworkTableEntry
	
	public OscarDrivetrain(SmartMC<?> leftMotor, SmartMC<?> rightMotor, Gyro gyro, OscarDTCharacteristics dtCharacteristics) {
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

		
		// TODO: investigate why this causes NPE
		// m_field.setRobotPose(m_pose);
	}
}
