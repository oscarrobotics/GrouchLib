package frc.team832.lib.logging.writers;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;
import java.util.TimeZone;
import java.util.stream.DoubleStream;

public abstract class CSVFileWriter {

	public static final String SEPARATOR = ", ";

	BufferedWriter logWriter;

	public CSVFileWriter(String fileName) {
		var folderPath = Path.of(Filesystem.getOperatingDirectory().getAbsolutePath(), "logs").toAbsolutePath().toString();
		var logFileName = "log_" + getDateTimeString() + "_" + fileName + ".csv";
		var filePath = Path.of(folderPath, logFileName).toAbsolutePath().toString();

		System.out.println("Initializing Log file: " + filePath);

		// create directories, if they don't exist
		var tempPathObj = new File(folderPath);
		//noinspection ResultOfMethodCallIgnored
		tempPathObj.mkdirs();

		try {
			// Open File
			logWriter = new BufferedWriter(new FileWriter(filePath, true));
			System.out.println("Initialized Log file: " + filePath);
			writeHeader();
		} catch (IOException e) {
			DriverStation.reportError("Error creating log file: " + e.getMessage(), false);
		}
	}

	protected void writeLine(String value) {
		write(value + "\n");
	}

	protected void writeLine(double value) {
		write(value + "\n");
	}

	protected void write(double value) {
		write(String.valueOf(value));
	}

	protected void write(String value) {
		try {
			logWriter.write(value);
		} catch (IOException e) {
			DriverStation.reportError("Error writing to log file: " + e.getMessage(), false);
		}
	}

	protected void writeWithSeparator(double value) {
		write(value + SEPARATOR);
	}

	protected void writeWithSeparator(String value) {
		write(value + SEPARATOR);
	}

	protected void writeValues(double... values) {
		write(doublesToString(values));
	}

	protected void writeValuesLine(double... values) {
		writeLine(doublesToString(values));
	}

	private String doublesToString(double... values) {
		StringJoiner sj = new StringJoiner(SEPARATOR);
		DoubleStream.of(values).forEach(x -> sj.add(String.valueOf(x)));
		return sj.toString();
	}

	protected void writeFPGATimestamp() {
		write(Timer.getFPGATimestamp() + ", ");
	}

	protected String getDateTimeString() {
		DateFormat df = new SimpleDateFormat("dd-MMM-yyyy_hh.mm.ssa");
		df.setTimeZone(TimeZone.getTimeZone("US/Central"));
		return df.format(new Date());
	}

	protected abstract void writeHeader();
}
