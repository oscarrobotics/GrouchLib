package frc.team832.lib.driverstation.controllers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;

public final class SWPrecisionPro extends GenericHID {
    
    public final POV POV;
    public final JoystickButton triggerButton = new JoystickButton(this, 1);

    public SWPrecisionPro(int port) {
        super(port);
        POV = new POV(this);
    }

    public POVButton getPOVButton(POV.Position pos) {
        return POV.getPOVButton(pos);
    }

    @Override
    public double getX(Hand hand) {
        return getX();
    }

    @Override
    public double getY(Hand hand) {
        return getY();
    }
}