package frc.team832.GrouchLib.Sensors;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class OscarCamera {

    private double _x;
    private double _y;
    private double _z;
    private double _xOffset;
    private double _yOffset;

    public OscarCamera(String tableName) {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable(tableName);
        NetworkTableEntry x = table.getEntry("X");
        NetworkTableEntry y = table.getEntry("Y");
        NetworkTableEntry z = table.getEntry("Z");
        NetworkTableEntry xOffset = table.getEntry("XOFFSET");
        NetworkTableEntry yOffset = table.getEntry("YOFFSET");

        x.addListener(event -> _x = event.value.getDouble(), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        y.addListener(event -> _y = event.value.getDouble(), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        x.addListener(event -> _z = event.value.getDouble(), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        x.addListener(event -> _xOffset = event.value.getDouble(), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
        x.addListener(event -> _yOffset = event.value.getDouble(), EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
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