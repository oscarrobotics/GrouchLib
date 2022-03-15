package frc.team832.lib.motors;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;

public class Powertrain {

	public final DCMotor motor;
	public final Gearbox gearbox;
	public final int motorCount;

	public Powertrain(Gearbox gearbox, DCMotor motor) {
		this(gearbox, motor, 1);
	}

	public Powertrain(Gearbox gearbox, DCMotor motor, int motorCount) {
		this.motor = motor;
		this.gearbox = gearbox;
		this.motorCount = motorCount;
	}

	public double getOutputSpeed() { return motor.freeSpeed / gearbox.getTotalReduction(); }

	public double getFreeCurrent() { return motor.freeCurrent * motorCount; }

	public double getStallCurrent() {
		return motor.stallCurrent * motorCount;
	}

	public double getStallTorque() {
		return (motor.stallTorque * motorCount) * gearbox.getTotalReduction();
	}
}
