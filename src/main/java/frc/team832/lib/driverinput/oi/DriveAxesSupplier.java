package frc.team832.lib.driverinput.oi;

import java.util.function.DoubleSupplier;

public class DriveAxesSupplier {
    private final DoubleSupplier leftSupplier;
    private final DoubleSupplier rightSupplier;
    private final DoubleSupplier rotationSupplier;

    public DriveAxesSupplier(DoubleSupplier leftSupplier, DoubleSupplier rightSupplier, DoubleSupplier rotation) {
        this.leftSupplier = leftSupplier;
        this.rightSupplier = rightSupplier;
        this.rotationSupplier = rotation;
    }

    public DriveAxesSupplier(DoubleSupplier leftSupplier, DoubleSupplier rightSupplier) {
        this.leftSupplier = leftSupplier;
        this.rightSupplier = rightSupplier;
        this.rotationSupplier = () -> 0;
    }

    public double getLeft() {
        return -leftSupplier.getAsDouble();
    }

    public double getRight() {
        return -rightSupplier.getAsDouble();
    }

    public double getRotation() { return rotationSupplier.getAsDouble(); }
}
