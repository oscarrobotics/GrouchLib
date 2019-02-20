package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;
import frc.team832.GrouchLib.Motors.ISmartMotor;

public class RotaryMechanism extends SmartMechanism {
    public RotaryMechanism(ISmartMotor smartMotor, OscarMechanismPositionList presetPositions) {
        super(smartMotor, presetPositions);
    }
}
