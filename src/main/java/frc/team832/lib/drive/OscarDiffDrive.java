package frc.team832.lib.drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.util.OscarMath;

public class OscarDiffDrive extends RobotDriveBase implements Sendable {
	private final SmartMC<?> m_leftMotor, m_rightMotor;

	public OscarDiffDrive(SmartMC<?> leftMotor, SmartMC<?> rightMotor) {
		m_leftMotor = leftMotor;
		m_rightMotor = rightMotor;
		setSafetyEnabled(false);
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
		
		xSpeed = OscarMath.signumPow(xSpeed, inputPow);
		zRotation = OscarMath.signumPow(zRotation, inputPow);
		
		var speeds = DifferentialDrive.arcadeDriveIK(xSpeed, zRotation, false);
		
		m_leftMotor.set(speeds.left);
		m_rightMotor.set(speeds.right);
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
		
		m_leftMotor.set(speeds.left);
		m_rightMotor.set(speeds.right);
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

		m_leftMotor.set(speeds.left * m_maxOutput);
		m_rightMotor.set(speeds.right * m_maxOutput);
	}
	
	public void stopMotor() {
		m_leftMotor.stop();
		m_rightMotor.stop();
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
