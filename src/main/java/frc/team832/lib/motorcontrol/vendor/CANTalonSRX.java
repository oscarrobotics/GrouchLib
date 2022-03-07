package frc.team832.lib.motorcontrol.vendor;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.util.ClosedLoopConfig;

public class CANTalonSRX implements SmartMC<WPI_TalonSRX> {
	private final WPI_TalonSRX _talon;
	private final Motor _motor;
	private final int _canID;

	private ControlMode _ctrlMode;
	private SupplyCurrentLimitConfiguration inputCurrentConfig = new SupplyCurrentLimitConfiguration(true, 40, 0, 0);

	public CANTalonSRX(int canId, Motor motor) {
		assert motor != Motor.kNEO && motor != Motor.kNEO550 && motor != Motor.kFalcon500 : "Invalid motor for CANTalonSRX!";

		_motor = motor;
		_talon = new WPI_TalonSRX(canId);
		_canID = canId;
		_ctrlMode = ControlMode.PercentOutput;

		CANDevice.addDevice(this, "Talon SRX");
	}

	@Override
	public Motor getMotor() {
		return _motor;
	}

	@Override
	public WPI_TalonSRX getBaseController() {
		return _talon;
	}

	@Override
	public void follow(SmartMC<?> masterMC) {
		if (!(masterMC instanceof CANSparkMax)) {
			_talon.follow(((CANTalonSRX)masterMC).getBaseController());
		} else {
			_ctrlMode = ControlMode.Follower;
		}
		_talon.set(_ctrlMode, masterMC.getCANID());
	}

	@Override
	public double getInputVoltage() {
		return getCANConnection() ? _talon.getBusVoltage() : Double.NaN;
	}

	@Override
	public double getOutputVoltage() {
		return getCANConnection() ? _talon.getMotorOutputVoltage() : Double.NaN;
	}

	@Override
	public double getInputCurrent() {
		return getCANConnection() ? _talon.getSupplyCurrent() : Double.NaN;
	}

	@Override
	public double getOutputCurrent() {
		return getCANConnection() ? _talon.getStatorCurrent() : Double.NaN;
	}

	@Override
	public void setNeutralMode(NeutralMode mode) {
		if (getCANConnection()) {
			_talon.setNeutralMode(mode == NeutralMode.kBrake ?
					com.ctre.phoenix.motorcontrol.NeutralMode.Brake :
					com.ctre.phoenix.motorcontrol.NeutralMode.Coast);
		}
	}

	@Override
	public int getCANID() {
		return _canID;
	}

	@Override
	public void wipeSettings() {
		if (getCANConnection()) {
			_talon.configFactoryDefault();
		}
	}

	@Override
	public void limitInputCurrent(int currentLimit) {
		if (getCANConnection()) {
			inputCurrentConfig.currentLimit = currentLimit;
			_talon.configSupplyCurrentLimit(inputCurrentConfig);
		}
	}

	@Override
	public double getSensorPosition() {
		return getCANConnection() ? (_talon.getSelectedSensorPosition() / 4096.0) : Double.NaN;
	}

	@Override
	public double getSensorVelocity() {
		return getCANConnection() ? ((_talon.getSelectedSensorVelocity() / 4096.0) * 600) : Double.NaN;
	}

	@Override
	public void setMotionProfileVelocity(double velocityPerSec) {
		if (getCANConnection()) {
			_talon.configMotionCruiseVelocity((int) (velocityPerSec / 10));
		}
	}

	@Override
	public void setTargetVelocity(double target) {
		if (getCANConnection()) {
			_talon.set(ControlMode.Velocity, target);
		}
	}

	@Override
	public void setTargetVelocity(double target, double arbFF) {
		if (getCANConnection()) {
			_talon.set(ControlMode.Velocity, target, DemandType.ArbitraryFeedForward, arbFF);
		}
	}

	@Override
	public void setTargetPosition(double target) {
		if (getCANConnection()) {
			_talon.set(ControlMode.Position, target);
		}
	}

	@Override
	public void setTargetPosition(double target, double arbFF) {
		if (getCANConnection()) {
			_talon.set(ControlMode.Position, target, DemandType.ArbitraryFeedForward, arbFF);
		}
	}

	@Override
	public void rezeroSensor() {
		if (getCANConnection()) {
			_talon.setSelectedSensorPosition(0);
		}
	}

	@Override
	public void setSensorPhase(boolean phase) {
		if (getCANConnection()) {
			_talon.setSensorPhase(phase);
		}
	}

	@Override
	public void set(double power) {
		_talon.set(power);
	}

	@Override
	public double get() {
		return getCANConnection() ? _talon.getMotorOutputPercent() : Double.NaN;
	}

	@Override
	public void stopMotor() {
		if (getCANConnection()) {
			_talon.set(ControlMode.PercentOutput, 0);
		}
	}

	@Override
	public void setInverted(boolean inverted) {
		if (getCANConnection()) {
			_talon.setInverted(inverted);
		}
	}

	@Override
	public boolean getInverted() {
		return getCANConnection() && _talon.getInverted();
	}

	@Override
	public void setPIDF(ClosedLoopConfig closedLoopConfig) {
		if (getCANConnection()) {
			_talon.config_kP(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkP());
			_talon.config_kI(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkI());
			_talon.config_kD(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkD());
			_talon.config_kF(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkF());
		}
	}

	@Override
	public boolean getCANConnection() {
		return _talon.getBusVoltage() > 0.0;
	}

	@Override
	public void disable() {
		_talon.disable();
		
	}
}
