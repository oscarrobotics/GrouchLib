package frc.team832.lib.motorcontrol.vendor;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.util.ClosedLoopConfig;

import static com.revrobotics.CANSparkMax.*;
import static com.revrobotics.SparkMaxPIDController.*;

import com.revrobotics.REVLibError;

public class CANSparkMax implements SmartMC<com.revrobotics.CANSparkMax, CANSparkMaxSimCollection> {

	private final com.revrobotics.CANSparkMax _spark;
	private final int _canID;
	private final RelativeEncoder _encoder;
	private final SparkMaxPIDController _pid;
	private final Motor _motor;

	private final CANSparkMaxSimCollection _simCollection;

	private double _openLoopSetpoint;
	private double _closedLoopSetpoint;
	private double _arbFF;

	public CANSparkMax(int canID, Motor motor) {
		assert motor != Motor.kFalcon500 : "Invalid motor for CANSparkMax!";

		_motor = motor;
		_canID = canID;

		// If motor is not a NEO or NEO550, it's a brushed motor
		MotorType motorType = (motor == Motor.kNEO || motor == Motor.kNEO550) ? MotorType.kBrushless : MotorType.kBrushed;

		_spark = new com.revrobotics.CANSparkMax(canID, motorType);
		_encoder = _spark.getEncoder();
		_pid = _spark.getPIDController();

		_simCollection = new CANSparkMaxSimCollection(this);

		CANDevice.addDevice(this, "Spark MAX");
	}

	@Override
	public Motor getMotor() {
		return _motor;
	}

	@Override
	public void follow(com.revrobotics.CANSparkMax master) {
		_spark.follow(master);
	}

	// public void follow(SmartMC<?, ?> masterMC, boolean invert) {
			// if (masterMC instanceof CANSparkMax) {/
				// _spark.follow((com.revrobotics.CANSparkMax) masterMC.getBaseController(), invert);
			// } else {
				// _spark.follow(ExternalFollower.kFollowerPhoenix, masterMC.getCANID(), invert);
			// }
		// }
	// }

	@Override
	public double getInputVoltage() {
		return getCANConnection() ? _spark.getBusVoltage() : Double.NaN;
	}

	@Override
	public double getOutputVoltage() {
		return getCANConnection() ? _spark.getAppliedOutput() : Double.NaN;
	}

	@Override
	public double getInputCurrent() {
		return getCANConnection() ? _spark.getOutputCurrent() : Double.NaN; // todo: any way to do this?
	}

	@Override
	public double getOutputCurrent() {
		return getCANConnection() ? _spark.getOutputCurrent() : Double.NaN;
	}

	@Override
	public void setNeutralMode(NeutralMode mode) {
		if (getCANConnection()) {
			_spark.setIdleMode(mode == NeutralMode.kBrake ?
					IdleMode.kBrake :
					IdleMode.kCoast);
		}
	}

	@Override
	public int getCANID() {
		return _canID;
	}

	@Override
	public void wipeSettings() {
		if (getCANConnection()) {
			_spark.restoreFactoryDefaults();
		}
	}

	@Override
	public void limitInputCurrent(int currentLimit) {
		if (getCANConnection()) {
			_spark.setSmartCurrentLimit(currentLimit);
		}
	}

	@Override
	public double getSensorPosition() {
		return getCANConnection() ? _encoder.getPosition() : Double.NaN;
	}

	@Override
	public double getSensorVelocity() {
		return getCANConnection() ? _encoder.getVelocity() : Double.NaN;
	}

	@Override
	public void setMotionProfileVelocity(double velocity) {
		if (getCANConnection()) {
			_pid.setSmartMotionMaxVelocity(velocity, _spark.getDeviceId());
		}
	}

	@Override
	public void setSensorPhase(boolean phase) {
		// No-op. sensor is always in phase with motor.
	}

	@Override
	public void rezeroSensor() {
		if (getCANConnection()) {
			_encoder.setPosition(0);
		}
	}

	public void setTargetVelocity(double target, double arbFF, ArbFFUnits arbFFUnits) {
		if (getCANConnection()) {
			if (target != _closedLoopSetpoint || arbFF != _arbFF) {
				_closedLoopSetpoint = target;
				_arbFF = arbFF;
				_pid.setReference(target, ControlType.kVelocity, 0, arbFF, arbFFUnits);
			}
		}
	}

	@Override
	public void setTargetVelocity(double target, double arbFF) {
		if (getCANConnection()) {
			if (target != _closedLoopSetpoint || arbFF != _arbFF) {
				_closedLoopSetpoint = target;
				_arbFF = arbFF;
				_pid.setReference(target, ControlType.kVelocity, 0, arbFF, Math.abs(arbFF) > 1 ? ArbFFUnits.kVoltage : ArbFFUnits.kPercentOut);
			}
		}
	}

	@Override
	public void setTargetVelocity(double target) {
		if (getCANConnection()) {
			if (target != _closedLoopSetpoint) {
				_closedLoopSetpoint = target;
				_pid.setReference(target, ControlType.kVelocity, 0, 0, ArbFFUnits.kPercentOut);
			}
		}
	}

	@Override
	public void setTargetPosition(double target, double arbFF) {
		if (target != _closedLoopSetpoint || arbFF != _arbFF) {
			_closedLoopSetpoint = target;
			_arbFF = arbFF;

			if (getCANConnection()) {
				_pid.setReference(target, ControlType.kPosition, 0, arbFF, Math.abs(arbFF) > 1 ? SparkMaxPIDController.ArbFFUnits.kVoltage : SparkMaxPIDController.ArbFFUnits.kPercentOut);
			}
		}
	}

	@Override
	public void setTargetPosition(double target) {
		if (target != _closedLoopSetpoint) {
			_closedLoopSetpoint = target;

			if (getCANConnection()) {
				_pid.setReference(target, ControlType.kPosition, 0, 0, SparkMaxPIDController.ArbFFUnits.kPercentOut);
			}
		}
	}

	@Override
	public void set(double power) {
		if (power != _openLoopSetpoint) {
			_openLoopSetpoint = power;
				_spark.set(power);
		}
	}

	@Override
	public double get() {
		return _openLoopSetpoint;
	}

	@Override
	public void stopMotor() {
		_spark.stopMotor();
	}

	@Override
	public void setInverted(boolean inverted) {
		if (getCANConnection()) {
			_spark.setInverted(inverted);
		}
	}

	@Override
	public boolean getInverted() {
		return _spark.getInverted();
	}

	@Override
	public com.revrobotics.CANSparkMax getBaseController() {
		return _spark;
	}

	@Override
	public void setPIDF(ClosedLoopConfig closedLoopConfig) {
		_pid.setP(closedLoopConfig.getkP());
		_pid.setI(closedLoopConfig.getkI());
		_pid.setD(closedLoopConfig.getkD());
		_pid.setFF(closedLoopConfig.getkF());
	}

	@Override
	public boolean getCANConnection() {
		return _spark.getLastError() != REVLibError.kCANDisconnected;
	}

	@Override
	public void disable() {
		_spark.disable();
		
	}

	@Override
	public CANSparkMaxSimCollection getSimCollection() {
		return _simCollection;
	}
}
