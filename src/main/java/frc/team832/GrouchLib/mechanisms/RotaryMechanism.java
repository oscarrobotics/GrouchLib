package frc.team832.GrouchLib.mechanisms;

import frc.team832.GrouchLib.mechanisms.Positions.MechanismPositionList;
import frc.team832.GrouchLib.motorcontrol.SmartMC;

public class RotaryMechanism extends SmartMechanism {
    public RotaryMechanism(SmartMC smartMotor, MechanismPositionList presetPositions) {
        super(smartMotor, presetPositions);
    }
}
