package frc.team832.lib.driverstation.controllers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.POVButton;

public class POV {	
	private final GenericHID controller;
	private final int povNo;
	
	private final POVButton povCenterButton;
	private final POVButton povUpButton;
    private final POVButton povUpRightButton;
    private final POVButton povRightButton;
    private final POVButton povDownRightButton;
    private final POVButton povDownButton;
    private final POVButton povDownLeftButton;
    private final POVButton povLeftButton;
	private final POVButton povUpLeftButton;
	
	POV(GenericHID controller) {
		this(controller, 0);
	}

	POV(GenericHID controller, int povNo) {
		this.povNo = povNo;
		this.controller = controller;
		povCenterButton = new POVButton(controller, Position.Center.value, povNo);
		povUpButton = new POVButton(controller, Position.Up.value, povNo);
		povUpRightButton = new POVButton(controller, Position.UpRight.value, povNo);
		povRightButton = new POVButton(controller, Position.Right.value, povNo);
		povDownRightButton = new POVButton(controller, Position.DownRight.value, povNo);
		povDownButton = new POVButton(controller, Position.Down.value, povNo);
		povDownLeftButton = new POVButton(controller, Position.DownLeft.value, povNo);
		povLeftButton = new POVButton(controller, Position.Left.value, povNo);
		povUpLeftButton = new POVButton(controller, Position.UpLeft.value, povNo);
	}

	public Position getPosition() {
		return Position.fromInt(controller.getPOV(povNo));
	}

	public boolean getPOVPressed(Position pos) {
		POVButton button = getPOVButton(pos);
		return button != null ? button.get() : false;
	}

	public POVButton getPOVButton(Position pos) {
        switch (pos) {
            case Up: 		return povUpButton;
            case UpRight:	return povUpRightButton;
            case Right:		return povRightButton;
			case DownRight: return povDownRightButton;
			case Down:		return povDownButton;
			case DownLeft:	return povDownLeftButton;
			case Left:		return povLeftButton;
			case UpLeft:	return povUpLeftButton;
			case Center:	return povCenterButton;
			default:		return null;
        }
    }

	public enum Position {
		Center(-1),
		Up(0),
		UpRight(45),
		Right(90),
		DownRight(135),
		Down(180),
		DownLeft(225),
		Left(270),
		UpLeft(315);
	
		private final int value;
	
		private Position(int value) {
			this.value = value;
		}

		public int getValue() { return value; }

		public boolean compare(int value) { return this.value == value; }

		public static Position fromInt(int value) {
			for (Position pos : values()) {
				if (pos.compare(value)) return pos;
			}
			return null;
		}
	
		public String toString() {
			switch(value) {
				case -1:  return "Center";
				case 0:   return "Up";
				case 45:  return "UpRight";
				case 90:  return "Right";
				case 135: return "DownRight";
				case 180: return "Down";
				case 225: return "DownLeft";
				case 270: return "Left";
				case 315: return "UpLeft";
				default:  return "Unknown"; // technically impossible
			}
		}
	}
}