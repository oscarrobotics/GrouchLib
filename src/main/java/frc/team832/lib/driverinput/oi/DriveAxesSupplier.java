package frc.team832.lib.driverinput.oi;

import java.util.function.DoubleSupplier;

public class DriveAxesSupplier {
    private final DoubleSupplier leftYSupplier;
    private final DoubleSupplier rightYSupplier;
    private final DoubleSupplier rightXSupplier;
    private final DoubleSupplier rotationSupplier;

    public DriveAxesSupplier(DoubleSupplier leftYSupplier, DoubleSupplier rightYSupplier, DoubleSupplier rotationSupplier) {
        this.leftYSupplier = leftYSupplier;
        this.rightYSupplier = rightYSupplier;
        this.rightXSupplier = () -> 0;
        this.rotationSupplier = rotationSupplier;
    }

    public DriveAxesSupplier(DoubleSupplier leftYSupplier, DoubleSupplier rightYSupplier) {
        this.leftYSupplier = leftYSupplier;
        this.rightYSupplier = rightYSupplier;
        this.rightXSupplier = () -> 0;
        this.rotationSupplier = () -> 0;
    }

    public DriveAxesSupplier(DoubleSupplier leftYSupplier, DoubleSupplier rightYSupplier, DoubleSupplier rightXSupplier, DoubleSupplier rotationSupplier) {
        this.leftYSupplier = leftYSupplier;
        this.rightYSupplier = rightYSupplier;
        this.rightXSupplier = rightXSupplier;
        this.rotationSupplier = rotationSupplier;
    }

    public double getLeftY() { return -leftYSupplier.getAsDouble(); }

    public double getRightY() {
        return -rightYSupplier.getAsDouble();
    }

    public double getRightX() {
        return -rightXSupplier.getAsDouble();
    }

    public double getRotation() { return rotationSupplier.getAsDouble(); }
}
