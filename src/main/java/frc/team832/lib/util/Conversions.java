package frc.team832.lib.util;

public class Conversions {
	private Conversions() {}

	public static int fromRpsToCtreVelocity(double rotPerSec, double encoderCpr) {
		double rotationsPer100ms = rotPerSec / 10;
		int ticksPer100ms = (int)(rotationsPer100ms * encoderCpr);
		return ticksPer100ms;
	}

	public static int fromRpmToCtreVelocity(double rpm, double encoderCpr) {
		return fromRpsToCtreVelocity(rpm / 60, encoderCpr);
	}

	public static double fromCtreVelocityToRpm(double ticksPer100ms, double encoderCpr) {
		double rotationsPer100ms = ticksPer100ms / encoderCpr;
		double rotationsPerMin = rotationsPer100ms * (60 * 10);
		return rotationsPerMin;
	}

	public static int fromRotationsToTicks(double rotations, double encoderCpr) {
		return (int)(rotations * encoderCpr);
	}

	public static double fromTicksToRotation(double ticks, double encoderCpr) {
		return ticks / encoderCpr;
	}
}
