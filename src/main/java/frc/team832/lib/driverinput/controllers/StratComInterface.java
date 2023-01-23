package frc.team832.lib.driverinput.controllers;

import java.util.EnumMap;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

@SuppressWarnings({"WeakerAccess"})
public class StratComInterface extends GenericHID {

	//Constructor
	public StratComInterface(int port) {
		super(port);
	}

	//Represent buttons on StratComInterface
	public enum Button {
		SC1(1),
		SC2(2),
		SC3(3),
		SC4(4),
		SC5(5),
		SC6(6),
		SCPlus(7),
		SCMinus(8),
		SCSideTop(9),
		SCSideMid(10),
		SCSideBot(11),
		ArcadeBlackLeft(12),
		ArcadeBlackRight(13),
		ArcadeWhiteLeft(14),
		ArcadeWhiteRight(15),
		SingleToggle(16),
		DoubleToggleDown(17),
		DoubleToggleUp(18),
		KeySwitch(19);

		public final int value;

		Button(int value) {
			this.value = value;
		}
	}

	private final EnumMap<Button, JoystickButton> m_buttons = new EnumMap<Button, JoystickButton>(Button.class);

	private JoystickButton build(Button button) {
		return new JoystickButton(this, button.value);
	}

	public JoystickButton sc1() {
		return m_buttons.computeIfAbsent(Button.SC1, this::build);
	}

	public JoystickButton sc2() {
		return m_buttons.computeIfAbsent(Button.SC2, this::build);
	}
	
	public JoystickButton sc3() {
		return m_buttons.computeIfAbsent(Button.SC3, this::build);
	}

	public JoystickButton sc4() {
		return m_buttons.computeIfAbsent(Button.SC4, this::build);
	}

	public JoystickButton sc5() {
		return m_buttons.computeIfAbsent(Button.SC5, this::build);
	}

	public JoystickButton sc6() {
		return m_buttons.computeIfAbsent(Button.SC6, this::build);
	}

	public JoystickButton scPlus() {
		return m_buttons.computeIfAbsent(Button.SCPlus, this::build);
	}

	public JoystickButton scMinus() {
		return m_buttons.computeIfAbsent(Button.SCMinus, this::build);
	}

	public JoystickButton scSideTop() {
		return m_buttons.computeIfAbsent(Button.SCSideTop, this::build);
	}

	public JoystickButton scSideMid() {
		return m_buttons.computeIfAbsent(Button.SCSideMid, this::build);
	}

	public JoystickButton scSideBot() {
		return m_buttons.computeIfAbsent(Button.SCSideBot, this::build);
	}

	public JoystickButton arcadeBlackLeft() {
		return m_buttons.computeIfAbsent(Button.ArcadeBlackLeft, this::build);
	}

	public JoystickButton arcadeBlackRight() {
		return m_buttons.computeIfAbsent(Button.ArcadeBlackRight, this::build);
	}

	public JoystickButton arcadeWhiteLeft() {
		return m_buttons.computeIfAbsent(Button.ArcadeWhiteLeft, this::build);
	}

	public JoystickButton arcadeWhiteRight() {
		return m_buttons.computeIfAbsent(Button.ArcadeWhiteRight, this::build);
	}

	public JoystickButton singleToggle() {
		return m_buttons.computeIfAbsent(Button.SingleToggle, this::build);
	}

	public JoystickButton doubleToggleUp() {
		return m_buttons.computeIfAbsent(Button.DoubleToggleUp, this::build);
	}

	public JoystickButton doubleToggleDown() {
		return m_buttons.computeIfAbsent(Button.DoubleToggleDown, this::build);
	}

	public JoystickButton keySwitch() {
		return m_buttons.computeIfAbsent(Button.KeySwitch, this::build);
	}


	// TODO: Fix later
	// public enum ThreeSwitchPos {
	// 	SWITCH_UP,
	// 	SWITCH_DOWN,
	// 	SWITCH_OFF
	// }

	// public ThreeSwitchPos getThreeSwitch () {
	// 	if (getDoubleToggleUp().get()) {
	// 		return ThreeSwitchPos.SWITCH_UP;
	// 	} else if (getDoubleToggleDown().get()) {
	// 		return ThreeSwitchPos.SWITCH_DOWN;
	// 	}
	// 	return ThreeSwitchPos.SWITCH_OFF;
	// }

	public double getLeftSlider() { return getRawAxis(0); }

	public double getRightSlider() { return getRawAxis(1); }
}