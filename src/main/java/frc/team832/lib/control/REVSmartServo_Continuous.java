package frc.team832.lib.control;

import edu.wpi.first.hal.FRCNetComm;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.PWM;

public class REVSmartServo_Continuous extends PWM {

	protected static final double kDefaultMaxServoPWM = 2.5;
	protected static final double kDefaultCenterServoPWM = 1.5;
	protected static final double kDefaultMinServoPWM = 0.5;

	/**
	 * Constructor.<br>
	 *
	 * <p>By default {@value #kDefaultMaxServoPWM} ms is used as the maxPWM value<br> By default
	 * {@value #kDefaultMinServoPWM} ms is used as the minPWM value<br>
	 *
	 * @param channel The PWM channel to which the servo is attached. 0-9 are on-board, 10-19 are on
	 *                the MXP port
	 */
	public REVSmartServo_Continuous(final int channel) {
		super(channel);
		setBounds(kDefaultMaxServoPWM, 0, 1.5, 0, kDefaultMinServoPWM);
		setPeriodMultiplier(PWM.PeriodMultiplier.k4X);

		HAL.report(FRCNetComm.tResourceType.kResourceType_Servo, getChannel() + 1);
		SendableRegistry.setName(this, "Servo", getChannel());
	}

	/**
	 * Set the servo power.
	 *
	 * <p>Servo values range from -1.0 to 1.0 corresponding to the range of counter-clockwise to clockwise.
	 *
	 * @param value Power from -1.0 to 1.0.
	 */
	public void set(double value) {
		setSpeed(value);
	}

	/**
	 * Get the servo power.
	 *
	 * <p>Servo values range from -1.0 to 1.0 corresponding to the range of counter-clockwise to clockwise.
	 *
	 * @return Power from -1.0 to 1.0.
	 */
	public double get() {
		return getSpeed();
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		builder.setSmartDashboardType("Servo");
		builder.addDoubleProperty("Value", this::get, this::set);
	}
}
