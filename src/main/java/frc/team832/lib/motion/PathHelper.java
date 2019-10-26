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
import frc.team832.lib.util.OscarMath;

public class PathHelper {

    private final DifferentialDriveKinematics m_kinematics;
    private final double m_maxVelocityMeters, m_maxAccelerationMetersSq;
    private final boolean m_reversed;

    public PathHelper(
                      DifferentialDriveKinematics kinematics,
                      double maxVelocityMeters,
                      double maxAccelerationMetersSq,
                      boolean reversed) {
        m_kinematics = kinematics;
        m_maxVelocityMeters = maxVelocityMeters;
        m_maxAccelerationMetersSq = maxAccelerationMetersSq;
        m_reversed = reversed;
    }

    public Trajectory generatePath(double startDegrees, List<Translation2d> waypoints, double endDegrees) {
        return generatePath(Rotation2d.fromDegrees(startDegrees), waypoints, Rotation2d.fromDegrees(endDegrees));
    }

    public Trajectory generatePath(Rotation2d startRadians, List<Translation2d> waypoints, Rotation2d endRadians) {
        int waypointCount = waypoints.size();

        if (waypointCount < 2) return null;

        var startWaypoint = waypoints.get(0);
        var endWaypoint = waypoints.get(waypoints.size() - 1);

        List<Translation2d> newWaypointList = new ArrayList<>();

        if (waypointCount == 2) {
            // do nothing
        } else if (waypointCount == 3) {
            newWaypointList.add(waypoints.get(1));
        } else {
            newWaypointList = waypoints.subList(1, waypoints.size() - 2);
        }

        var startPose = new Pose2d(startWaypoint, startRadians);
        var endPose = new Pose2d(endWaypoint, endRadians);

        Timer genTimer = new Timer();
        genTimer.start();

        var traj = TrajectoryGenerator.generateTrajectory(startPose, newWaypointList, endPose, m_kinematics,
                0, 0, m_maxVelocityMeters, m_maxAccelerationMetersSq, m_reversed);

        genTimer.stop();

        System.out.printf("Generated Path in %.2fms\n", genTimer.get());
        return traj;
    }

    public static Pose2d mirrorPose(Pose2d pose2d){
        return new Pose2d(pose2d.getTranslation().getX(), 8.23 - pose2d.getTranslation().getY(), new Rotation2d(-pose2d.getRotation().getRadians()));
    }
}