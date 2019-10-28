package frc.team832.lib.motion;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

public class PathHelper {

    private final DifferentialDriveKinematics m_kinematics;
    private final double m_maxVelocityMeters, m_maxAccelerationMetersSq;

    public PathHelper(
                      DifferentialDriveKinematics kinematics,
                      double maxVelocityMeters,
                      double maxAccelerationMetersSq) {
        m_kinematics = kinematics;
        m_maxVelocityMeters = maxVelocityMeters;
        m_maxAccelerationMetersSq = maxAccelerationMetersSq;
    }

    public Trajectory generatePath(double startDegrees, List<Translation2d> waypoints, double endDegrees) {
        return generatePath(startDegrees, waypoints, endDegrees, false);
    }

    public Trajectory generatePath(double startDegrees, List<Translation2d> waypoints, double endDegrees, boolean reversed) {
        return generatePath(Rotation2d.fromDegrees(startDegrees), waypoints, Rotation2d.fromDegrees(endDegrees), reversed);
    }

    public Trajectory generatePath(Rotation2d startRotation, List<Translation2d> waypoints, Rotation2d endRotation) {
        return generatePath(startRotation, waypoints, endRotation, false);
    }

    public Trajectory generatePath(Rotation2d startRotation, List<Translation2d> waypoints, Rotation2d endRotation, boolean reversed) {
        int waypointCount = waypoints.size();

        if (waypointCount < 2) return null;

        var startWaypoint = waypoints.get(0);
        var endWaypoint = waypoints.get(waypointCount - 1);

        List<Translation2d> newWaypointList = new ArrayList<>();

        if (waypointCount == 2) {
            // do nothing
        } else if (waypointCount == 3) {
            newWaypointList.add(waypoints.get(1));
        } else {
            newWaypointList = waypoints.subList(1, waypointCount - 2);
        }

        var startPose = new Pose2d(startWaypoint, startRotation);
        var endPose = new Pose2d(endWaypoint, endRotation);

        Timer genTimer = new Timer();
        genTimer.start();

        var traj = TrajectoryGenerator.generateTrajectory(startPose, newWaypointList, endPose, m_kinematics,
                0, 0, m_maxVelocityMeters, m_maxAccelerationMetersSq, reversed);

        genTimer.stop();

        System.out.printf("Generated Path in %.2fms\n", genTimer.get());
        return traj;
    }

    public static Pose2d mirrorPose(Pose2d pose2d){
        return new Pose2d(pose2d.getTranslation().getX(), 8.23 - pose2d.getTranslation().getY(), new Rotation2d(-pose2d.getRotation().getRadians()));
    }
}