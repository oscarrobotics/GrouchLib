package frc.team832.lib.logging.writers;

import edu.wpi.first.wpilibj.system.LinearSystemLoop;
import edu.wpi.first.wpiutil.math.numbers.*;

public class ArmStateSpaceLogWriter extends CSVFileWriter {

    public ArmStateSpaceLogWriter(String systemName) {
        super(systemName + "-ArmStateSpace");
    }

    @Override
    protected void writeHeader() {
        writeLine("Timestamp, Reference, PositionState, VelocityState, Input, Output");
    }

    public void logSystemState(LinearSystemLoop<N2, N1, N1> loop, double output) {
        writeFPGATimestamp();
        writeWithSeparator(loop.getNextR(0));
        writeWithSeparator(loop.getXHat(0));
        writeWithSeparator(loop.getXHat(1));
        writeWithSeparator(loop.getU(0));
        writeWithSeparator(output);
    }
}
