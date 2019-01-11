package frc.team832.GrouchLib.Motors;

/**
 * NOT IMPLEMENTED
 * Awaiting release of SPARK MAX API.
 */
public class OscarCANSparkMax {
}

/*
public class OscarCANSparkMax implements IOscarSmartMotor {

    private CANSparkMax _sparkMax;
    private ExternalFollower _followType;

    public OscarCANSparkMax(int canId, MotorType mType){
        _sparkMax = new CANSparkMax(canId, mType);
    }

    @Override
    public void setMode(ControlMode mode) {
        return; // SPARK MAX API currently only supports PercentOutput
    }

    public void setFollowType(ExternalFollower followType) { _followType = followType; }

    @Override
    public void follow(int masterMotorID) { _sparkMax.follow(_followType, masterMotorID); }

    @Override
    public double getPosition() { return _sparkMax.getEncoder(); }

    @Override
    public double getInputVoltage() { return _sparkMax.getBusVoltage(); }

    @Override
    public double getOutputVoltage() { return _sparkMax.getAppliedOutput() }

    @Override
    public double getOutputCurrent() { return _sparkMax.getOutputCurrent() }

    @Override
    public void set(double value) { _sparkMax.set(value); }

    @Override
    public double get() { return _sparkMax.get(); }

    @Override
    public void setInverted(boolean isInverted) { _sparkMax.setInverted(isInverted); }

    @Override
    public boolean getInverted() { return _sparkMax.getInverted(); }

    @Override
    public void disable() { _sparkMax.disable(); }

    @Override
    public void stopMotor() { _sparkMax.stopMotor; }
}
*/
