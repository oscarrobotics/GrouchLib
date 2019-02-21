package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.GrouchLib.Motors.SmartMotor;

public class LinearMechanism extends SmartMechanism {
    public LinearMechanism(SmartMotor smartMotor, MechanismPositionList presetPositions) {
	    super(smartMotor, presetPositions);
    }
}
