package edu.wpi.first.wpilibj.trajectory;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TrajectoryTest {

    private static final List<Pose2d> testPath = Arrays.asList(
            new Pose2d(new Translation2d(5.326, 9.697), Rotation2d.fromDegrees(0.0)),
            new Pose2d(new Translation2d(18.651, 6.997), Rotation2d.fromDegrees(-23.0)),
            new Pose2d(new Translation2d(22.13, 2.612), Rotation2d.fromDegrees(-148.0))
    );

    private static final DifferentialDriveKinematics differentialDriveKinematics = new DifferentialDriveKinematics(0.741);
    private static final double startVelocityMetersPerSecond = 0;
    private static final double endVelocityMetersPerSecond = 0;
    private static final double maxVelocityMetersPerSecond = 3.5;
    private static final double maxAccelerationMetersPerSecondSq = 10;

    private static Trajectory testTraj;
    private static TrajectoryConfig config = new TrajectoryConfig(maxVelocityMetersPerSecond, maxAccelerationMetersPerSecondSq);

    @Before
    public void init() {
        var waypoints = Collections.singletonList(testPath.get(1).getTranslation());
        testTraj = TrajectoryGenerator.generateTrajectory(testPath.get(0), waypoints, testPath.get(2), config);
    }

    @Test
    public void trajectoryToStringTest() {
        System.out.println(testTraj.toString());
    }
}
