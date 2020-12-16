package frc.team832.lib.logging.readers;

import frc.team832.lib.logging.formats.FlywheelStateSpaceLogLine;

public class FlywheelStateSpaceLogReader extends CSVFileReader<FlywheelStateSpaceLogLine> {

    public FlywheelStateSpaceLogReader(String filePath) {
        super(filePath);
    }

    @Override
    protected FlywheelStateSpaceLogLine parseLine(String line) {
        return new FlywheelStateSpaceLogLine(line);
    }
}
