package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Motors.IOscarSimpleMotor;

public class OscarSimpleMechanism {

    private IOscarSimpleMotor _motor;

    public OscarSimpleMechanism(IOscarSimpleMotor motor) {
        _motor = motor;
    }

    public void set(double value) {
        _motor.set(value);
    }

    public double get() {
        return _motor.get();
    }
}
