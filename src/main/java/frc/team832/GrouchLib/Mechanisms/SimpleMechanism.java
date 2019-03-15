package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Motors.SimpleMotor;

public class SimpleMechanism {

    private SimpleMotor _motor;

    public SimpleMechanism(SimpleMotor motor) {
        _motor = motor;
    }

    public void set(double value) {
        _motor.set(value);
    }

    public double get() {
        return _motor.get();
    }

    public void stop() { _motor.stopMotor(); }
}
