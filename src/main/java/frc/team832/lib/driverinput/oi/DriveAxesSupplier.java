package frc.team832.lib.driverinput.oi;

import java.util.function.DoubleSupplier;

public class DriveAxesSupplier {
    private final DoubleSupplier leftSupplier;
    private final DoubleSupplier rightSupplier;

    public DriveAxesSupplier(DoubleSupplier leftSupplier, DoubleSupplier rightSupplier) {
        this.leftSupplier = leftSupplier;
        this.rightSupplier = rightSupplier;
    }

    public double getLeft() {
        return leftSupplier.getAsDouble();
    }

    public double getRight() {
        return rightSupplier.getAsDouble();
    }
}
