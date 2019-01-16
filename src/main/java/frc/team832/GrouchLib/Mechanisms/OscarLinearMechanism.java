package frc.team832.GrouchLib.Mechanisms;

        import frc.team832.GrouchLib.Motors.IOscarSmartMotor;

public class OscarLinearMechanism extends OscarSmartMechanism {

    public OscarLinearMechanism(IOscarSmartMotor smartMotor) {
        super(smartMotor);
    }

    public OscarLinearMechanism(IOscarSmartMotor smartMotor, OscarMechanismPosition[] presetPositions) {
        super(smartMotor, presetPositions);
    }

    @Override
    public double getPositionInches() {
        return 0;
    }

    @Override
    public void setPositionInches() {

    }
}
