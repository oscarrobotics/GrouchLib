package frc.team832.lib.drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import frc.team832.lib.motorcontrol.SimpleMC;
import frc.team832.lib.util.OscarMath;

/**
 * A class for driving differential drive/skid-steer drive platforms such as the Kit of Parts drive
 * base, "tank drive", or West Coast Drive.
 *
 * 
 * 
 * <p>These drive bases typically have drop-center / skid-steer with two or more wheels per side
 * (e.g., 6WD or 8WD). This class takes a {@link frc.team832.lib.motorcontrol.SmartMC} per side. Use the
 * {@link frc.team832.lib.motorcontrol.SmartMC#follow(SmartMC)} method to link motors on a given side 
 * to each other, as follows.
 * 
 * <pre><code>
 * public class DrivetrainSubsystem extends SubsystemBase {
 *   SmartMC m_leftFalconOne = new CANTalonFX(1);
 *   SmartMC m_leftFalconTwo = new CANTalonFX(2);
 *   SmartMC m_rightFalconOne = new CANTalonFX(3);
 *   SmartMC m_rightFalconTwo = new CANTalonFX(4);
 *   OscarDiffDrive m_diffDrive;
 * 
 *   public DrivetrainSubsystem() {
 *     m_leftFalconTwo.follow(leftFalconOne);
 *     m_rightFalconTwo.follow(rightFalconOne);
 * 
 *     m_diffDrive = new OscarDiffDrive(m_leftFalconOne, m_rightFalconOne);
 *   }
 * }
 * </code></pre>
 * 
 * <p>Alternatively, see {@link frc.team832.lib.drive.OscarDrivetrain}, which manages this class for you.
 */
public final class OscarDiffDrive extends RobotDriveBase implements Sendable {
	public static final double kDefaultQuickStopThreshold = 0.3;
	public static final double kDefaultQuickStopAlpha = 0.1;
	
	private final SimpleMC<?> m_leftMotor, m_rightMotor;
	private final SimpleMotorFeedforward m_leftFF, m_rightFF;
	private final boolean m_useFF;

	private double m_quickStopThreshold = kDefaultQuickStopThreshold;
	private double m_quickStopAlpha = kDefaultQuickStopAlpha;
	private double m_quickStopAccumulator = 0.0;

	private double m_maxVelocity = 0;

	public OscarDiffDrive(SimpleMC<?> leftMotor, SimpleMC<?> rightMotor) {
		m_leftMotor = leftMotor;
		m_rightMotor = rightMotor;
		m_leftFF = new SimpleMotorFeedforward(0, 0);
		m_rightFF =  new SimpleMotorFeedforward(0, 0);
		m_useFF = false;
		setSafetyEnabled(false);
	}

	public OscarDiffDrive(
			SimpleMC<?> leftMotor, SimpleMC<?> rightMotor,
			SimpleMotorFeedforward leftFeedforward,
			SimpleMotorFeedforward rightFeedforward,
			double maxVelocity
		) {
		m_leftMotor = leftMotor;
		m_rightMotor = rightMotor;
		m_leftFF = leftFeedforward;
		m_rightFF = rightFeedforward;
		boolean leftFFInvalid = leftFeedforward.kv == 0 || leftFeedforward.ka == 0;
		boolean rightFFInvalid = rightFeedforward.kv == 0 || rightFeedforward.ka == 0;
		m_useFF = !(leftFFInvalid || rightFFInvalid);
		m_maxVelocity = maxVelocity;
		setSafetyEnabled(false);
	}

	/**
	 * Set the max velocity to scale inputs to for 
	 * feed forward controlled open-loop driving.
	 * @param maxVelocity Maximum velocity (in the same units as feed forward).
	 */
	public void setMaxFFVelocity(double maxVelocity) {
		m_maxVelocity = maxVelocity;
	}

	/**
   * Arcade drive method for differential drive platform. The calculated values will be squared to
   * decrease sensitivity at low speeds.
   *
   * @param xSpeed The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *     positive.
   */
	public void arcadeDrive(double xSpeed, double zRotation) {
		arcadeDrive(xSpeed, zRotation, 1);	
	}
	
	/**
   * Arcade drive method for differential drive platform. The calculated values will be squared to
   * decrease sensitivity at low speeds.
   *
   * @param xSpeed The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
   *     positive.
	 * @param inputPow The exponent by which to modify the inputs to change sensitivity.
   */
	public void arcadeDrive(double xSpeed, double zRotation, double inputPow) {
		xSpeed = MathUtil.applyDeadband(xSpeed, m_deadband);
		zRotation = MathUtil.applyDeadband(zRotation, m_deadband);
		
		// xSpeed = OscarMath.signumPow(xSpeed, inputPow);
		// zRotation = OscarMath.signumPow(zRotation, inputPow);
		
		var speeds = DifferentialDrive.arcadeDriveIK(xSpeed, zRotation, false);
		
		// if (m_useFF) {
			// double leftFFEffortVolts = m_leftFF.calculate(speeds.left * m_maxVelocity);
			// double rightFFEffortVolts = m_rightFF.calculate(speeds.left * m_maxVelocity);

			// m_leftMotor.setVoltage(leftFFEffortVolts);
			// m_rightMotor.setVoltage(rightFFEffortVolts);
		// } else {
			m_leftMotor.set(speeds.left);
			m_rightMotor.set(speeds.right);
		// }		
	}
	
	/**
   * Curvature drive method for differential drive platform.
   *
   * <p>The rotation argument controls the curvature of the robot's path rather than its rate of
   * heading change. This makes the robot more controllable at high speeds.
   *
   * @param xSpeed The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation The normalized curvature [-1.0..1.0]. Clockwise is positive.
   * @param allowTurnInPlace If set, overrides constant-curvature turning for turn-in-place
   *     maneuvers. zRotation will control turning rate instead of curvature.
   */
	public void curvatureDrive(double xSpeed, double zRotation, boolean allowTurnInPlace) {
		curvatureDrive(xSpeed, zRotation, allowTurnInPlace, 1);
	}
	
	/**
   * Curvature drive method for differential drive platform.
   *
   * <p>The rotation argument controls the curvature of the robot's path rather than its rate of
   * heading change. This makes the robot more controllable at high speeds.
   *
   * @param xSpeed The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param zRotation The normalized curvature [-1.0..1.0]. Clockwise is positive.
   * @param allowTurnInPlace If set, overrides constant-curvature turning for turn-in-place
   *     maneuvers. zRotation will control turning rate instead of curvature.
	 * @param inputPow The exponent by which to modify the inputs to change sensitivity.
   */
	public void curvatureDrive(double xSpeed, double zRotation, boolean allowTurnInPlace, double inputPow) {
		xSpeed = MathUtil.applyDeadband(xSpeed, m_deadband);
		zRotation = MathUtil.applyDeadband(zRotation, m_deadband);
		
		xSpeed = OscarMath.signumPow(xSpeed, inputPow);
		zRotation = OscarMath.signumPow(zRotation, inputPow);
		
		var speeds = DifferentialDrive.curvatureDriveIK(xSpeed, zRotation, allowTurnInPlace);
		
		if (m_useFF) {
			double leftFFEffortVolts = m_leftFF.calculate(speeds.left * m_maxVelocity);
			double rightFFEffortVolts = m_rightFF.calculate(speeds.left * m_maxVelocity);

			m_leftMotor.setVoltage(leftFFEffortVolts);
			m_rightMotor.setVoltage(rightFFEffortVolts);
		} else {
			m_leftMotor.set(speeds.left);
			m_rightMotor.set(speeds.right);
		}		
	}
	
	/**
   * Tank drive method for differential drive platform. The calculated values will be squared to
   * decrease sensitivity at low speeds.
   *
   * @param leftSpeed The robot's left side speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param rightSpeed The robot's right side speed along the X axis [-1.0..1.0]. Forward is
   *     positive.
   */
	public void tankDrive(double leftSpeed, double rightSpeed) {
		tankDrive(leftSpeed, rightSpeed, 1);
	}
	
	/**
   * Tank drive method for differential drive platform. The calculated values will be squared to
   * decrease sensitivity at low speeds.
   *
   * @param leftSpeed The robot's left side speed along the X axis [-1.0..1.0]. Forward is positive.
   * @param rightSpeed The robot's right side speed along the X axis [-1.0..1.0]. Forward is
   *     positive.
	 * @param inputPow The exponent by which to modify the inputs to change sensitivity.
	 */
	public void tankDrive(double leftSpeed, double rightSpeed, double inputPow) {
		leftSpeed = MathUtil.applyDeadband(leftSpeed, m_deadband);
		rightSpeed = MathUtil.applyDeadband(rightSpeed, m_deadband);
		
		leftSpeed = OscarMath.signumPow(leftSpeed, inputPow);
		rightSpeed = OscarMath.signumPow(rightSpeed, inputPow);

		var speeds = DifferentialDrive.tankDriveIK(leftSpeed, rightSpeed, false);
		
		if (m_useFF) {
			double leftFFEffortVolts = m_leftFF.calculate(speeds.left * m_maxVelocity);
			double rightFFEffortVolts = m_rightFF.calculate(speeds.right * m_maxVelocity);

			m_leftMotor.setVoltage(leftFFEffortVolts);
			m_rightMotor.setVoltage(rightFFEffortVolts);
		} else {
			m_leftMotor.set(speeds.left * m_maxOutput);
			m_rightMotor.set(speeds.right * m_maxOutput);
		}
		
	}
	
	public void stopMotor() {
		m_leftMotor.stopMotor();
		m_rightMotor.stopMotor();
	}

	@Override
	public String getDescription() {
		return "OscarDiffDrive";
	}
	
	@Override
	public void initSendable(SendableBuilder builder) {
		builder.setSmartDashboardType("DifferentialDrive");
    builder.setActuator(true);
    builder.setSafeState(this::stopMotor);
    builder.addDoubleProperty("Left Motor Speed", m_leftMotor::get, m_leftMotor::set);
    builder.addDoubleProperty("Right Motor Speed", m_rightMotor::get, m_rightMotor::set);
		
	}
}
