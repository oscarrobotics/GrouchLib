package frc.team832.GrouchLib.driverstation.controllers;

import edu.wpi.first.hal.FRCNetComm;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class DancePad extends GenericHID {

    public enum DancePadButtons {
        kUpButton(1),
        kDownButton(2),
        kLeftButton(3),
        kRightButton(4),
        kXButton(7),
        kOButton(8),
        kBackButton(9),
        kSelectButton(10);

        public final int VALUE;

        DancePadButtons(int value) { this.VALUE = value; }
    }

    public final JoystickButton upButton = new JoystickButton(this, DancePadButtons.kUpButton.VALUE);
    public final JoystickButton downButton = new JoystickButton(this, DancePadButtons.kDownButton.VALUE);
    public final JoystickButton leftButton = new JoystickButton(this, DancePadButtons.kLeftButton.VALUE);
    public final JoystickButton rightButton = new JoystickButton(this, DancePadButtons.kRightButton.VALUE);
    public final JoystickButton xButton = new JoystickButton(this, DancePadButtons.kXButton.VALUE);
    public final JoystickButton oButton = new JoystickButton(this, DancePadButtons.kOButton.VALUE);
    public final JoystickButton backButton = new JoystickButton(this, DancePadButtons.kBackButton.VALUE);
    public final JoystickButton selectButton = new JoystickButton(this, DancePadButtons.kSelectButton.VALUE);


    public DancePad(int port) {
        super(port);
    }

    @Override
    public double getX(Hand hand) {
        return 0;
    }

    @Override
    public double getY(Hand hand) {
        return 0;
    }
}
