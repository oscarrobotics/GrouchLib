package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;
import frc.team832.GrouchLib.Motors.IOscarSmartMotor;
import java.util.List;

public class OscarRotaryMechanism extends OscarSmartMechanism {
    public OscarRotaryMechanism(IOscarSmartMotor smartMotor, OscarMechanismPositionList presetPositions) {
        super(smartMotor, presetPositions);
    }
}
