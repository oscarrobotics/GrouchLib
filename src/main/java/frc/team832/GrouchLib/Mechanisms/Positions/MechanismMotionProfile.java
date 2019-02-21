package frc.team832.GrouchLib.Mechanisms.Positions;

import edu.wpi.first.wpilibj.Filesystem;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;

import java.io.File;

public class MechanismMotionProfile {

    private String startIndex, stopIndex;
    private Trajectory targetTraj;
    private double[][] talonTraj;

    public MechanismMotionProfile(String currentIndex, String targetIndex, String mechanism) {
        startIndex = currentIndex;
        stopIndex = targetIndex;
        String targetTrajName = String.format("%s_To_%s_%s.csv", currentIndex, targetIndex, mechanism);
        String targetTrajPath = Filesystem.getDeployDirectory() + targetTrajName;

        System.out.println("Loading profile: " + targetTrajName);

        targetTraj = Pathfinder.readFromCSV(new File(targetTrajPath));
        talonTraj = pathfinderFormatToTalon(targetTraj);
    }

    public Trajectory pathfinderTrajectory() {
        return targetTraj;
    }

    public double[][] talonTrajectory() {
        return talonTraj;
    }

    public int length(){
        return targetTraj.length();
    }

    // gets the final position based on the index from the positionlist the profile was based on.
    // should work? just have to be sure indexes are all the same...
    public double finalPosition(MechanismPositionList mechanismPositionList)  {
        return mechanismPositionList.getByIndex(stopIndex).getTarget();
    }

    private static double[][] pathfinderFormatToTalon(Trajectory t) {
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
