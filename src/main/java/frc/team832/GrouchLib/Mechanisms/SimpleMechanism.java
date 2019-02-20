package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Motors.ISimpleMotor;

public class SimpleMechanism {

    private ISimpleMotor _motor;

    public SimpleMechanism(ISimpleMotor motor) {
        _motor = motor;
    }

    public void set(double value) {
        _motor.set(value);
    }

    public double get() {
        return _motor.get();
    }
}
