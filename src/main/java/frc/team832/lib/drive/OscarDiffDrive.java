package frc.team832.lib.drive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import frc.team832.lib.motorcontrol.SmartMC;

public class OscarDiffDrive extends RobotDriveBase implements Sendable {
	public static final double kDefaultDeadband = 0.02;
  public static final double kDefaultMaxOutput = 1.0;

  protected double m_deadband = kDefaultDeadband;
  protected double m_maxOutput = kDefaultMaxOutput;


	private final SmartMC<?> m_leftMotor, m_rightMotor;

	public OscarDiffDrive(SmartMC<?> leftMotor, SmartMC<?> rightMotor) {
		m_leftMotor = leftMotor;
		m_rightMotor = rightMotor;
		setSafetyEnabled(false);
	}

	public void tankDrive(double leftSpeed, double rightSpeed) {
		leftSpeed = MathUtil.applyDeadband(leftSpeed, m_deadband);
		rightSpeed = MathUtil.applyDeadband(rightSpeed, m_deadband);

		var speeds = DifferentialDrive.tankDriveIK(leftSpeed, rightSpeed, false);

		m_leftMotor.set(speeds.left * m_maxOutput);
		m_rightMotor.set(speeds.right * m_maxOutput);
	}

	public void stopMotor() {
		m_leftMotor.stop();
		m_rightMotor.stop();
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		builder.setSmartDashboardType("DifferentialDrive");
    builder.setActuator(true);
    builder.setSafeState(this::stopMotor);
    builder.addDoubleProperty("Left Motor Speed", m_leftMotor::get, m_leftMotor::set);
    builder.addDoubleProperty(
        "Right Motor Speed", () -> m_rightMotor.get(), x -> m_rightMotor.set(x));
		
	}

	@Override
	public String getDescription() {
		return "OscarDiffDrive";
	}
	
}
