package frc.team832.lib.logging.formats;

import frc.team832.lib.logging.writers.CSVFileWriter;

public class FlywheelStateSpaceLogLine {
	public final double fpgaTimestamp;
	public final double reference;
	public final double state;
	public final double input;
	public final double output;

	public FlywheelStateSpaceLogLine(String fileLine) {
		String[] data = fileLine.split(CSVFileWriter.SEPARATOR);

		fpgaTimestamp = Double.parseDouble(data[0]);
		reference = Double.parseDouble(data[1]);
		state = Double.parseDouble(data[2]);
		input = Double.parseDouble(data[3]);
		output = Double.parseDouble(data[4]);
	}

	public FlywheelStateSpaceLogLine(
		double fpgaTimestamp,
		double reference,
		double state,
		double input,
		double output
	) {
		this.fpgaTimestamp = fpgaTimestamp;
		this.reference = reference;
		this.state = state;
		this.input = input;
		this.output = output;
	}
}
