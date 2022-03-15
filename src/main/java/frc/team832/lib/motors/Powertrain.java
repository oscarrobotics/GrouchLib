package frc.team832.lib.motors;

public class Powertrain {

	public final Motor motor;
	public final Gearbox gearbox;
	public final int motorCount;

	public Powertrain(Gearbox gearbox, Motor motor) {
		this(gearbox, motor, 1);
	}

	public Powertrain(Gearbox gearbox, Motor motor, int motorCount) {
		this.motor = motor;
		this.gearbox = gearbox;
		this.motorCount = motorCount;
	}

	public double getOutputSpeed() { return motor.freeSpeedRPM / gearbox.getTotalReduction(); }

	public double getFreeCurrent() { return motor.freeCurrentAmps * motorCount; }

	public double getStallCurrent() {
		return motor.stallCurrentAmps * motorCount;
	}

	public double getStallTorque() {
		return (motor.stallTorqueNewtonMeters * motorCount) * gearbox.getTotalReduction();
	}
}
