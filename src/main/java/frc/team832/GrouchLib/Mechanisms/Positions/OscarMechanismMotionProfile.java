package frc.team832.GrouchLib.Mechanisms.Positions;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;

import java.io.File;

public class OscarMechanismMotionProfile {

    private String trajPath;
    private Trajectory targetTraj;

    public OscarMechanismMotionProfile(String currentIndex, String targetIndex, String mechanism) {
        // RocketCargoTop_To_RocketCargoBottom_Elevator
        String targetTrajPath = String.format("%s_To_%s_%s.csv", currentIndex, targetIndex, mechanism);
        targetTraj = Pathfinder.readFromCSV(new File("File path here" + trajPath + ".csv"));
    }

    public Trajectory getTargetTrajectory() {
        return targetTraj;
    }

    public int trajectoryLength(){
        return targetTraj.length();
    }

    public static double[][] pathfinderFormatToTalon(Trajectory t) {
        int i = 0;
        double[][] list = new double[t.length()][3];
        for (Trajectory.Segment s : t.segments) {
            list[i][0] = s.position;
            list[i][1] = s.velocity;
            list[i][2] = s.dt;
            i++;
        }
        return list;
    }
}
