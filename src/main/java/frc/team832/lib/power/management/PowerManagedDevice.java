package frc.team832.lib.power.management;

import frc.team832.lib.util.math.OscarMath;

public abstract class PowerManagedDevice {
    private int requestedCurrentAmps = 0;
    private double userWeight = 0;
    private double constrainedCurrentAmps = 0;
    protected double internalWeight = 0;

    public PowerManagedDevice() {
        this(0, 0);
    }

    public PowerManagedDevice(double weight) {
        this(weight, 0);
    }

    public PowerManagedDevice(double weight, int requestedCurrentAmps) {
        setWeight(weight);
        setRequestedCurrentAmps(requestedCurrentAmps);
    }

    public void setRequestedCurrentAmps(int requestedCurrentAmps) {
        this.requestedCurrentAmps = OscarMath.clip(requestedCurrentAmps, 0, 500);
    }

    public void setWeight(double weight) {
        this.userWeight = OscarMath.clip(weight, 0.0001, 1);
    }

    protected void applyConstrainedCurrentAmps(double constrainedCurrentAmps) {
        this.constrainedCurrentAmps = OscarMath.round(constrainedCurrentAmps, 0);
        applyCurrentLimit();
    }

    public abstract void applyCurrentLimit();

    public int getRequestedCurrentAmps() {
        return requestedCurrentAmps;
    }

    public double getConstrainedCurrentAmps() { return constrainedCurrentAmps; }

    public double getWeight() {
        return userWeight;
    }
}
