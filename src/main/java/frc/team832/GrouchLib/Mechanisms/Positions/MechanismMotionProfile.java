package frc.team832.GrouchLib.Mechanisms.Positions;

import edu.wpi.first.wpilibj.Filesystem;
import jaci.pathfinder.Trajectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class MechanismMotionProfile {

    private String startIndex, stopIndex;
    private double[][] talonTraj;

    public MechanismMotionProfile(String currentIndex, String targetIndex, String mechanism) {
        startIndex = currentIndex;
        stopIndex = targetIndex;
        String targetTrajName = String.format("%s_to_%s_%s.csv", currentIndex, targetIndex, mechanism);
        String targetTrajPath = String.format("%s/%s",Filesystem.getDeployDirectory(), targetTrajName);
        System.out.println("Loading profile: " + targetTrajPath);
        File pathFile = new File(targetTrajPath);
        talonTraj = loadProfileFromCSV(pathFile);
        System.out.println("Successfully loaded profile: " + targetTrajName);
    }

    public double[][] talonTrajectory() {
        return talonTraj;
    }

    public int length(){
        return talonTraj.length;
    }

    // gets the final position based on the index from the positionlist the profile was based on.
    // should work? just have to be sure indexes are all the same...
    public double finalPosition(MechanismPositionList mechanismPositionList)  {
        return mechanismPositionList.getByIndex(stopIndex).getTarget();
    }


    private static double[][] loadProfileFromCSV(File file) {
        BufferedReader fileReader;

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        try
        {
            String line;
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(file));

            int lineCounter = 0;
            int lineCount = (int) fileReader.lines().count();
            double[][] csvData = new double[lineCount][3];

            //Read the file line by line
            while ((line = fileReader.readLine()) != null)
            {
                //Get all tokens available in line
                String[] tokens = line.split(DELIMITER);
                double[] doubleValues = Arrays.stream(tokens)
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                System.arraycopy(doubleValues, 0, csvData[lineCounter], 0, 3);
                lineCounter++;
            }

            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return csvData;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
