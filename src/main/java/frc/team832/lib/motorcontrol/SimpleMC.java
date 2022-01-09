package frc.team832.lib.motorcontrol;

// import edu.wpi.first.wpilibj.motorcontrol;
import frc.team832.lib.motors.Motor;

public interface SimpleMC<B> {

	/**
	 *
	 * @param power setpoint from -1.0 to 1.0
	 */
	void set(double power);

	/**
	 *
	 * @return setpoint
	 */
	double get();

	void stop();

	void setInverted(boolean inverted);

	boolean getInverted();

	Motor getMotor();

	B getBaseController();

	/**
	 * Gets the current output voltage of the controller.
	 *
	 * @return Output in Volts.
	 */
	double getOutputVoltage();
}
