package frc.team832.lib.motorcontrol;

import frc.team832.lib.util.ClosedLoopConfig;

public interface SmartMC<B, S extends SmartMCSimCollection> extends SimpleMC<B> {

	void follow(B masterMC);

	/**
	 * Gets the current input voltage of the controller.
	 *
	 * @return Input in Volts.
	 */
	double getInputVoltage();

	/**
	 * Gets the input current of the controller.
	 *
	 * @return Input in Amps.
	 */
	double getInputCurrent();

	/**
	 * Gets the current output current of the controller.
	 *
	 * @return Output in Amps.
	 */
	double getOutputCurrent();

	/**
	 * Sets the NeutralMode of the motor. (Brake or Coast)
	 *
	 * @param mode NeutralMode to set.
	 */
	void setNeutralMode(NeutralMode mode);

	int getCANID();

	void wipeSettings();

	void limitInputCurrent(int currentLimit);

	double getSensorPosition();

	double getSensorVelocity();

	void setMotionProfileVelocity(double velocity);

	void rezeroSensor();

	void setTargetPosition(double target);

	void setTargetPosition(double target, double arbFF);

	void setTargetVelocity(double target);

	void setTargetVelocity(double target, double arbFF);

	/**
	 * Sets the sensor phase of the encoder relative to the motor.
	 * Note: this is a no-op when using any brushless motor (NEO, NEO550, Falcon 500)
	 * @param phase Sensor phase
	 */
	void setSensorPhase(boolean phase);

	void setPIDF(ClosedLoopConfig closedLoopConfig);

	void enableForwardSoftLimit(boolean enable);
	void setForwardSoftLimit(double limit);
	void enableReverseSoftLimit(boolean enable);
	void setReverseSoftLimit(double limit);

	S getSimCollection();	

	boolean getCANConnection();
}