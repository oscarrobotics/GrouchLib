package frc.team832.lib.driverinput.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class Attack3 extends Joystick {
	/**
	 * Construct an instance of a joystick. The joystick index is the USB port on the drivers
	 * station.
	 *
	 * @param port The port on the Driver Station that the joystick is plugged into.
	 */
	public Attack3 (int port) {
		super(port);
	}

	public final JoystickButton trigger = new JoystickButton(this, 1);
	public final JoystickButton two = new JoystickButton(this, 2);
	public final JoystickButton three = new JoystickButton(this, 3);
	public final JoystickButton four = new JoystickButton(this, 4);
	public final JoystickButton five = new JoystickButton(this, 5);
	public final JoystickButton six = new JoystickButton(this, 6);
	public final JoystickButton seven = new JoystickButton(this, 7);
	public final JoystickButton eight = new JoystickButton(this, 8);
	public final JoystickButton nine = new JoystickButton(this, 9);
	public final JoystickButton ten = new JoystickButton(this, 10);
	public final JoystickButton eleven = new JoystickButton(this, 11);
}
