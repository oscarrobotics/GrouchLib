package frc.team832.lib.logging.readers;

import edu.wpi.first.wpilibj.DriverStation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class CSVFileReader<T> {

    private final List<T> dataPoints = new ArrayList<>();

    public CSVFileReader(String filePath) {

        System.out.println("Loading Log file: " + filePath);

        try (BufferedReader logReader = new BufferedReader(new FileReader(filePath))) {
            System.out.println("Loaded Log file: " + filePath);

            // read header line
            logReader.readLine();

            // read in data lines
            String line;
            while((line = logReader.readLine()) != null) {
                try {
                    dataPoints.add(parseLine(line));
                } catch (Exception ex) {
                    DriverStation.reportError("Error reading log line: " + ex.getMessage(), false);
                }
            }

            System.out.println("Read Log file: " + filePath);
        } catch (FileNotFoundException e) {
            DriverStation.reportError("Error loading log file: File not found!", false);
        } catch (IOException e) {
            DriverStation.reportError("Error loading log file: " + e.getMessage(), false);
        }
    }

    protected abstract T parseLine(String line);

    public List<T> getDataPoints() {
        return dataPoints;
    }
}