package frc.team832.lib.logging.formats;

import edu.wpi.first.math.numbers.*;
import edu.wpi.first.math.system.LinearSystemLoop;
import edu.wpi.first.wpilibj.Timer;

public class ArmStateSpaceLogLine {
	public final double fpgaTimestamp;
	public final double reference;
	public final double positionState;
	public final double velocityState;
	public final double input;
	public final double positionOutput;
	public final double velocityOutput;

	public ArmStateSpaceLogLine(String line) {
		String[] data = line.replaceAll("^[,\\s]+", "").split("[,\\s]+");

		fpgaTimestamp = Double.parseDouble(data[0]);
		reference = Double.parseDouble(data[1]);
		positionState = Double.parseDouble(data[2]);
		velocityState = Double.parseDouble(data[3]);
		input = Double.parseDouble(data[4]);
		positionOutput = Double.parseDouble(data[5]);
		velocityOutput = Double.parseDouble(data[6]);
	}

	public ArmStateSpaceLogLine(double fpgaTimestamp, double reference, double positionState, double velocityState, double input, double positionOutput, double velocityOutput) {
		this.fpgaTimestamp = fpgaTimestamp;
		this.reference = reference;
		this.positionState = positionState;
		this.velocityState = velocityState;
		this.input = input;
		this.positionOutput = positionOutput;
		this.velocityOutput = velocityOutput;
	}

	public static ArmStateSpaceLogLine fromLoop(double timestamp, LinearSystemLoop<N2, N1, N1> loop, double positionOutput, double velocityOutput) {
		return new ArmStateSpaceLogLine(
			timestamp,
			loop.getNextR(0),
			loop.getXHat(0),
			loop.getXHat(1),
			loop.getU(0),
			positionOutput,
			velocityOutput
		);
	}

	public static ArmStateSpaceLogLine fromLoop(LinearSystemLoop<N2, N1, N1> loop, double positionOutput, double velocityOutput) {
		return new ArmStateSpaceLogLine(
			Timer.getFPGATimestamp(),
			loop.getNextR(0),
			loop.getXHat(0),
			loop.getXHat(1),
			loop.getU(0),
			positionOutput,
			velocityOutput
		);
	}

	@Override
	public String toString() {
		return fpgaTimestamp +
			"," + reference +
			"," + positionState +
			"," + velocityState +
			"," + input +
			"," + positionOutput +
			"," + velocityOutput +
			"\n";
	}
}
