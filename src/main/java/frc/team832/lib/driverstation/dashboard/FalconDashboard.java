package frc.team832.lib.driverstation.dashboard;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.util.Units;
import frc.team832.lib.util.GeometryUtil;

public class FalconDashboard {
    private FalconDashboard() {}

    private static final NetworkTable falconTable = NetworkTableInstance.getDefault().getTable("Live_Dashboard");
    private static final NetworkTableEntry falconPoseXEntry = falconTable.getEntry("robotX");
    private static final NetworkTableEntry falconPoseYEntry = falconTable.getEntry("robotY");
    private static final NetworkTableEntry falconPoseHeadingEntry = falconTable.getEntry("robotHeading");
    private static final NetworkTableEntry falconIsFollowingPathEntry = falconTable.getEntry("isFollowingPath");
    private static final NetworkTableEntry falconPathXEntry = falconTable.getEntry("pathX");
    private static final NetworkTableEntry falconPathYEntry = falconTable.getEntry("pathY");
    private static final NetworkTableEntry falconPathHeadingEntry = falconTable.getEntry("pathHeading");

    public static void updateRobotPose2d(Pose2d pose2d) {
        var rawPose = new GeometryUtil.RawPose(pose2d);

        falconPoseXEntry.setDouble(Units.metersToFeet(rawPose.xMeters));
        falconPoseYEntry.setDouble(Units.metersToFeet(rawPose.yMeters));
        falconPoseHeadingEntry.setDouble(rawPose.radians);
    }

    public static void startPath() {
        falconIsFollowingPathEntry.setBoolean(true);
    }

    public static void stopPath() {
        falconIsFollowingPathEntry.setBoolean(false);
    }

    public static void updatePathState(Trajectory.State trajState) {
        var rawPose = new GeometryUtil.RawPose(trajState.poseMeters);

        falconPathXEntry.setDouble(Units.metersToFeet(rawPose.xMeters));
        falconPathYEntry.setDouble(Units.metersToFeet(rawPose.yMeters));
        falconPathHeadingEntry.setDouble(rawPose.radians);
    }

}
