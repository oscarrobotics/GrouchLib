package frc.team832.lib.motion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.Timer;

public class PathHelper {

	public static Trajectory generatePath(Pose2d startPose, Pose2d endPose, TrajectoryConfig config) {
		return generatePath(startPose, List.of(), endPose, config);
	}

	public static Trajectory generatePath(Pose2d startPose, List<Translation2d> waypoints, Pose2d endPose, TrajectoryConfig config) {
		return TrajectoryGenerator.generateTrajectory(startPose, waypoints, endPose, config);
	}

	public static Trajectory generatePath(Rotation2d startRotation, List<Translation2d> waypoints, Rotation2d endRotation, boolean reversed, TrajectoryConfig config) {
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

		var traj = TrajectoryGenerator.generateTrajectory(startPose, newWaypointList, endPose, config);

		genTimer.stop();

		System.out.printf("Generated Path in %.2fms\n", genTimer.get());
		return traj;
	}

	public static Pose2d mirrorPose2d(Pose2d pose2d) {
		return new Pose2d(mirrorTranslation2d(pose2d.getTranslation()), new Rotation2d(-pose2d.getRotation().getRadians()));
	}

	public static Translation2d mirrorTranslation2d(Translation2d translation2d) {
		return new Translation2d(translation2d.getX(), 8.23 - translation2d.getY());
	}

	public static List<Translation2d> mirrorTranslation2dList(List<Translation2d> translation2dList) {
		return translation2dList.stream().map(PathHelper::mirrorTranslation2d).collect(Collectors.toList());
	}
}