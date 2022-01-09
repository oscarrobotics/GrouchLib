package frc.team832.lib.driverinput.controllers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class DancePad extends GenericHID {

	public enum Button {
		kUpButton(1),
		kDownButton(2),
		kLeftButton(3),
		kRightButton(4),
		kXButton(7),
		kOButton(8),
		kBackButton(9),
		kSelectButton(10);

		public final int value;

		Button(int value) { this.value = value; }
	}

	public final JoystickButton upButton = new JoystickButton(this, Button.kUpButton.value);
	public final JoystickButton downButton = new JoystickButton(this, Button.kDownButton.value);
	public final JoystickButton leftButton = new JoystickButton(this, Button.kLeftButton.value);
	public final JoystickButton rightButton = new JoystickButton(this, Button.kRightButton.value);
	public final JoystickButton xButton = new JoystickButton(this, Button.kXButton.value);
	public final JoystickButton oButton = new JoystickButton(this, Button.kOButton.value);
	public final JoystickButton backButton = new JoystickButton(this, Button.kBackButton.value);
	public final JoystickButton selectButton = new JoystickButton(this, Button.kSelectButton.value);

	public DancePad(int port) {
		super(port);
	}
}
