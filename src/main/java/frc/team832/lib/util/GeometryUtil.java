package frc.team832.lib.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;

public class GeometryUtil {

	public static class RawPose {
		public final double xMeters;
		public final double yMeters;
		public final double radians;

		public RawPose(Pose2d pose2d) {
			Translation2d translation = pose2d.getTranslation();
			xMeters = translation.getX();
			yMeters = translation.getY();
			radians = pose2d.getRotation().getRadians();
		}
	}
}
