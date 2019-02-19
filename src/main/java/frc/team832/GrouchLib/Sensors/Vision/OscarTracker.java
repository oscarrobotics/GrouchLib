package frc.team832.GrouchLib.Sensors.Vision;

public abstract class OscarTracker {

    private TrackerObjectType _type;
    private int _index;
    private double _x;
    private double _y;
    private double _z;
    private double _xOffset;
    private double _yOffset;

    public TrackerObjectType getType() {
        return _type;
    }

    public int getIndex() {
        return _index;
    }

    public double getX() {
        return _x;
    }

    public double getY() {
        return _y;
    }

    public double getZ() {
        return _z;
    }

    public double getXOffset() {
        return _xOffset;
    }

    public double getYOffset() {
        return _yOffset;
    }
}