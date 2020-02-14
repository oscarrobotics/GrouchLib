package frc.team832.lib.sensors.base;

import frc.team832.lib.util.OscarMath;

public abstract class DistanceSensor {

    private final double minRangeMeters;
    private final double maxRangeMeters;

    public DistanceSensor(double minRangeMeters, double maxRangeMeters) {
        this.minRangeMeters = minRangeMeters;
        this.maxRangeMeters = maxRangeMeters;
    }

    public abstract double getDistanceMeters();

    public double getPercentageDistance() {
        return OscarMath.clipMap(getDistanceMeters(), minRangeMeters, maxRangeMeters, 0, 1);
    }
}
