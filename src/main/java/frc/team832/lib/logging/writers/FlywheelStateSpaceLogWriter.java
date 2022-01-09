package frc.team832.lib.logging.writers;

public class FlywheelStateSpaceLogWriter extends CSVFileWriter {

	public FlywheelStateSpaceLogWriter(String systemName) {
		super(systemName + "-FlywheelStateSpace");
	}

	@Override
	protected void writeHeader() {
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
