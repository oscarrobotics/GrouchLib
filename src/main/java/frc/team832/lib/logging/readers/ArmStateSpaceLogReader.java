package frc.team832.lib.logging.readers;

import frc.team832.lib.logging.formats.ArmStateSpaceLogLine;

public class ArmStateSpaceLogReader extends CSVFileReader<ArmStateSpaceLogLine> {
	public ArmStateSpaceLogReader(String filePath) {
		super(filePath);
	}

	@Override
	protected ArmStateSpaceLogLine parseLine(String line) {
		return new ArmStateSpaceLogLine(line);
	}
}
