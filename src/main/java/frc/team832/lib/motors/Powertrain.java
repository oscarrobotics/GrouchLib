package frc.team832.lib.motors;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;

public class Powertrain {

	public final DCMotor motor;
	public final Gearbox gearbox;
	private final int _motorCount;

	public Powertrain(Gearbox gearbox, DCMotor motor) {
		this(gearbox, motor, 1);
	}

	public Powertrain(Gearbox gearbox, DCMotor motor, int motorCount) {
		this.motor = motor;
		this.gearbox = gearbox;
		_motorCount = motorCount;
	}

	public int getMotorCount() { return _motorCount; }

	public double getOutputSpeed() {
		var motorFreeRPM = Units.radiansPerSecondToRotationsPerMinute(motor.freeSpeedRadPerSec);
		return motorFreeRPM / gearbox.getTotalReduction();
	}

	public double getFreeCurrent() { return motor.freeCurrentAmps * _motorCount; }

	public double getStallCurrent() {
		return motor.stallCurrentAmps * _motorCount;
	}

	public double getStallTorque() {
		return (motor.stallTorqueNewtonMeters * _motorCount) * gearbox.getTotalReduction();
	}
}
