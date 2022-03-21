package frc.team832.lib.drive;

import frc.team832.lib.motorcontrol.SimpleMC;

public class OscarSimpleDrivetrain {
	public final SimpleMC<?> m_leftMotor;
	public final SimpleMC<?> m_rightMotor;
	private final OscarDiffDrive m_diffDrive;

	public OscarSimpleDrivetrain(SimpleMC<?> leftMotor, SimpleMC<?> rightMotor) {
		m_leftMotor = leftMotor;
		m_rightMotor = rightMotor;
		
		m_diffDrive = new OscarDiffDrive(leftMotor, rightMotor);
		m_diffDrive.setDeadband(0.05);
	}
	
	/**
	 * Get differential drive controller for tele-op control.
	 * @return {@link frc.team832.lib.drive.OscarDiffDrive} 
	 */
	public OscarDiffDrive getDiffDrive() {
		return m_diffDrive;
	}

	public void stop() {
		m_diffDrive.stopMotor();
	}
}