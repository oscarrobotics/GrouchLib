package frc.team832.lib.motion;

import java.util.List;

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
        if (waypoints.size() < 2) return null;

        var startWaypoint = waypoints.get(0);
        var endWaypoint = waypoints.get(waypoints.size() - 1);

        waypoints.remove(0);
        waypoints.remove(waypoints.size() - 1);

        var startPose = new Pose2d(startWaypoint, startRadians);
        var endPose = new Pose2d(endWaypoint, endRadians);
        return TrajectoryGenerator.generateTrajectory(startPose, waypoints, endPose, m_kinematics,
                0, 0, m_maxVelocityMeters, m_maxAccelerationMetersSq, m_reversed);
    }
}