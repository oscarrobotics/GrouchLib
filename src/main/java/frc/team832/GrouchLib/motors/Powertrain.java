package frc.team832.GrouchLib.motors;

public class Powertrain {

    public final Motor motor;
    public final Gearbox gearbox;
    private final int _motorCount;

    public Powertrain(Gearbox gearbox, Motor motor) {
        this(gearbox, motor, 1);
    }

    public Powertrain(Gearbox gearbox, Motor motor, int motorCount) {
        this.motor = motor;
        this.gearbox = gearbox;
        _motorCount = motorCount;
    }

    public double getOutputSpeed() {
        return gearbox.getTotalReduction() * motor.freeSpeed;
    }

    public double getFreeCurrent() {
        return motor.freeCurrent * _motorCount;
    }

    public double getStallCurrent() {
        return motor.stallCurrent * _motorCount;
    }

    public double getStallTorque() {
        return (motor.stallTorque * _motorCount) * gearbox.getTotalReduction();
    }
}
