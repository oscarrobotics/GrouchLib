package frc.team832.GrouchLib.Sensors.Vision;

import java.util.List;

abstract class VisionTracker {
    List<TrackerObject> detectedObjects;

    public TrackerObject getNearestObject() {
        return detectedObjects.get(0);
    }
}