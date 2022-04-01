package frc.team832.lib.util;

public class Conversions {
	public static double fromRpmToCtreVelocity(double rpm, double encoderCpr) {
		double rotationsPer100ms = rpm / (60 * 10);
		double ticksPer100ms = rotationsPer100ms * encoderCpr;
		return ticksPer100ms;
	}

	public static double fromCtreVelocityToRpm(double ticksPer100ms, double encoderCpr) {
		double rotationsPer100ms = ticksPer100ms / encoderCpr;
		double rotationsPerMin = rotationsPer100ms * (60 * 10);
		return rotationsPerMin;
	}
}
