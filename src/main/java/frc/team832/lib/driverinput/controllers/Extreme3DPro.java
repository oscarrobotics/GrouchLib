package frc.team832.lib.driverinput.controllers;

import edu.wpi.first.wpilibj.buttons.JoystickButton;

@SuppressWarnings("unused")
public class Extreme3DPro extends Attack3 {
	/**
	 * Construct an instance of a joystick. The joystick index is the USB port on the drivers
	 * station.
	 *
	 * @param port The port on the Driver Station that the joystick is plugged into.
	 */
	public Extreme3DPro (int port) {
		super(port);
		setTwistChannel(2);
		setThrottleChannel(3);
	}

	public final JoystickButton twelve = new JoystickButton(this, 12);
}
