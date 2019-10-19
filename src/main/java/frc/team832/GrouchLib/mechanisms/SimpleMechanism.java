package frc.team832.GrouchLib.mechanisms;

import frc.team832.GrouchLib.motorcontrol.SimpleMC;

public class SimpleMechanism {

    private SimpleMC _motor;

    public SimpleMechanism(SimpleMC motor) {
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
