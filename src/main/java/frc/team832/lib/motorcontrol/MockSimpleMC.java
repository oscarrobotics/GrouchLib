package frc.team832.lib.motorcontrol;

import frc.team832.lib.motors.Motor;

public class MockSimpleMC implements SimpleMC<MockSimpleMC> {

	private final Motor m_motor;

	private boolean m_inverted;
	private double m_power;

	public MockSimpleMC(Motor motor) {
		m_motor = motor;
	}

	@Override
	public void set(double power) {
		m_power = power;
		m_power *= m_inverted ? -1 : 1;
	}

	@Override
	public double get() {
		return m_power;
	}

	@Override
	public void stop() {
		m_power = 0;		
	}

	@Override
	public void setInverted(boolean inverted) {
		m_inverted = inverted;
	}

	@Override
	public boolean getInverted() {
		return m_inverted;
	}

	@Override
	public Motor getMotor() {
		return m_motor;
	}

	@Override
	public MockSimpleMC getBaseController() {
		return this;
	}

	@Override
	public double getOutputVoltage() {
		return m_power * 12;
	}
}
