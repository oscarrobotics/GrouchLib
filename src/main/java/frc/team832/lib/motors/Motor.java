package frc.team832.lib.motors;

public class Motor {
    public final int freeSpeed;
    public final double stallTorque;
    public final double stallCurrent;
    public final double freeCurrent;

    public Motor(int freeSpeed, double stallTorque, double stallCurrent, double freeCurrent) {
        this.freeSpeed = freeSpeed;
        this.stallTorque = stallTorque;
        this.stallCurrent = stallCurrent;
        this.freeCurrent = freeCurrent;
    }
}
