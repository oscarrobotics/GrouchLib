package frc.team832.lib.logging.formats;

import frc.team832.lib.logging.writers.CSVFileWriter;

public class ArmStateSpaceLogLine {
    public final double fgpaTimestamp;
    public final double reference;
    public final double positionState;
    public final double velocityState;
    public final double input;
    public final double output;

    public ArmStateSpaceLogLine(String line) {
        String[] data = line.split(CSVFileWriter.SEPARATOR);

        fgpaTimestamp = Double.parseDouble(data[0]);
        reference = Double.parseDouble(data[0]);
        positionState = Double.parseDouble(data[0]);
        velocityState = Double.parseDouble(data[0]);
        input = Double.parseDouble(data[0]);
        output = Double.parseDouble(data[0]);
    }

    public ArmStateSpaceLogLine(double fgpaTimestamp, double reference, double positionState, double velocityState, double input, double output) {

        this.fgpaTimestamp = fgpaTimestamp;
        this.reference = reference;
        this.positionState = positionState;
        this.velocityState = velocityState;
        this.input = input;
        this.output = output;
    }
}
