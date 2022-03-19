package frc.team832.lib.motors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlywheelPowertrainTest {
	private static final Motor m_motor = Motor.kFalcon500;
	private static final Gearbox m_gearbox = new Gearbox(1.0 / 2.0);
	private static final WheeledPowerTrain m_wheeledPowerTrain = new WheeledPowerTrain(m_gearbox, m_motor, 1, 4.0, 1.0);

	@Test
	public void wheelSpeedFromMotorSpeedTest() {
		double desiredMotorSpeed = 5000;
		double expectedWheelSpeed = 2500;
		double actualWheelSpeed = m_wheeledPowerTrain.calcWheelFromMotor(desiredMotorSpeed);

		assertEquals(expectedWheelSpeed, actualWheelSpeed, 0.009f, "Wheel Speed FAIL");
	}
}
