package frc.team832.lib.logging;

public class FlywheelStateSpaceLogger extends CSVFileWriter {

    public FlywheelStateSpaceLogger(String systemName) {
        super(systemName + "-FlywheelStateSpace");
    }

    @Override
    void writeHeader() {
        writeLine("Timestamp, Reference, State, Input, Output");
    }

    public void logSystemState(double reference, double state, double input, double output) {
        writeFPGATimestamp();
        writeWithSeparator(reference);
        writeWithSeparator(state);
        writeWithSeparator(input);
        writeLine(output);
    }
}
