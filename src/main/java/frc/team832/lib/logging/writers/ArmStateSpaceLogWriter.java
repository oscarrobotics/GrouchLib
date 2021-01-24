package frc.team832.lib.logging.writers;

import edu.wpi.first.wpilibj.system.LinearSystemLoop;
import edu.wpi.first.wpiutil.math.numbers.*;
import frc.team832.lib.logging.formats.ArmStateSpaceLogLine;

public class ArmStateSpaceLogWriter extends CSVFileWriter {

    public ArmStateSpaceLogWriter(String systemName) {
        super(systemName + "-ArmStateSpace");
    }

    @Override
    protected void writeHeader() {
        writeLine("Timestamp, Reference, PositionState, VelocityState, Input, PositionOutput, VelocityOutput");
    }

    public void logSystemState(LinearSystemLoop<N2, N1, N1> loop, double positionOutput, double velocityOutput) {
        write(ArmStateSpaceLogLine.fromLoop(loop, positionOutput, velocityOutput).toString());
    }
}
