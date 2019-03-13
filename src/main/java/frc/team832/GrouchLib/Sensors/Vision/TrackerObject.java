package frc.team832.GrouchLib.Sensors.Vision;

import org.opencv.core.Point;
import org.opencv.core.Rect2d;

public class TrackerObject {

	private TrackerObjectType _type;
	private int _index;

	private Point _objCentroid;
	private Rect2d _objFace;

	public TrackerObject(TrackerObjectType type, int index, double distInches, Point objCentroid, Rect2d objFace) {
		_type = _type;
		_index = _index;
		_objCentroid = objCentroid;
		_objFace = objFace;
	}
}
