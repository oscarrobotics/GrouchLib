package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;
import frc.team832.GrouchLib.Motors.ISmartMotor;

public class LinearMechanism extends SmartMechanism {
    public LinearMechanism(ISmartMotor smartMotor, OscarMechanismPositionList presetPositions) {
	    super(smartMotor, presetPositions);
    }
}
