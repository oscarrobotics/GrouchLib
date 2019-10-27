package edu.wpi.first.wpilibj.geometry;

import org.junit.Test;

public class Pose2dTest {

    private static final Pose2d testPose1 = new Pose2d(new Translation2d(3, 0), Rotation2d.fromDegrees(50));
    private static final Pose2d testPose2 = new Pose2d(new Translation2d(7.5, 3.6), Rotation2d.fromDegrees(0));

    @Test
    public void toStringTest() {
        System.out.println(testPose1.toString());
        System.out.println(testPose2.toString());
    }
}
