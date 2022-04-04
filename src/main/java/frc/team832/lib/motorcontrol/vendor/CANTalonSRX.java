package frc.team832.lib.motorcontrol.vendor;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.motorcontrol.SmartMCSim;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.util.ClosedLoopConfig;
import frc.team832.lib.util.Conversions;

public class CANTalonSRX implements SmartMC<WPI_TalonSRX> {
	private final WPI_TalonSRX _talon;
	private final Motor _motor;
	private final int _canID;

	// TODO: this is not always the case!
	public final int ENCODER_CPR = 4096;

	private ControlMode _ctrlMode;
	private SupplyCurrentLimitConfiguration inputCurrentConfig = new SupplyCurrentLimitConfiguration(true, 40, 0, 0);
	private final SmartMCSim _sim;

	public CANTalonSRX(int canId, Motor motor) {
		assert motor != Motor.kNEO && motor != Motor.kNEO550 && motor != Motor.kFalcon500 : "Invalid motor for CANTalonSRX!";

		_motor = motor;
		_talon = new WPI_TalonSRX(canId);
		_canID = canId;
		_ctrlMode = ControlMode.Disabled;

		var _talonSim = _talon.getSimCollection();
		_sim = new SmartMCSim() {
			@Override
			public void setBusVoltage(double voltage) {
				_talonSim.setBusVoltage(voltage);
			}

			@Override
			public void setSensorPosition(double position) {
				var ticks = Conversions.fromRotationsToTicks(position, ENCODER_CPR);
				_talonSim.setQuadratureRawPosition(ticks);
			}

			@Override
			public void setSensorVelocity(double velocity) {
				var ticksPer100ms = Conversions.fromRpmToCtreVelocity(velocity, ENCODER_CPR);
				_talonSim.setQuadratureVelocity(ticksPer100ms);
			}
		};

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
	public void follow(WPI_TalonSRX master) {
		_talon.follow(master);
	}

	@Override
	public double getInputVoltage() {
		return _talon.getBusVoltage();
	}

	@Override
	public double getOutputVoltage() {
		return _talon.getMotorOutputVoltage();
	}

	@Override
	public double getInputCurrent() {
		return _talon.getSupplyCurrent();
	}

	@Override
	public double getOutputCurrent() {
		return _talon.getStatorCurrent();
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
		var err = _talon.configFactoryDefault();
		notifyOnError(err, "wipeSettings");
	}

	@Override
	public void limitInputCurrent(int currentLimit) {
		inputCurrentConfig.currentLimit = currentLimit;
		var err = _talon.configSupplyCurrentLimit(inputCurrentConfig);
		notifyOnError(err, "limitInputCurrent");
	}

	@Override
	public double getSensorPosition() {
		return Conversions.fromTicksToRotation(_talon.getSelectedSensorPosition(), ENCODER_CPR);
	}

	@Override
	public double getSensorVelocity() {
		double ticksPer100ms = _talon.getSelectedSensorVelocity();
		return Conversions.fromCtreVelocityToRpm(ticksPer100ms, ENCODER_CPR);
	}

	@Override
	public void setMotionProfileVelocity(double rpm) {
		int ticksPer100ms = Conversions.fromRpmToCtreVelocity(rpm, ENCODER_CPR);
		var err = _talon.configMotionCruiseVelocity(ticksPer100ms, 5);
		notifyOnError(err, "setMotionProfileVelocity");
	}

	@Override
	public void setTargetVelocity(double rpm) {
		int ticksPer100ms = Conversions.fromRpmToCtreVelocity(rpm, ENCODER_CPR);
		_talon.set(ControlMode.Velocity, ticksPer100ms);
	}

	@Override
	public void setTargetVelocity(double rpm, double arbFF) {
		int ticksPer100ms = Conversions.fromRpmToCtreVelocity(rpm, ENCODER_CPR);
		_talon.set(ControlMode.Velocity, ticksPer100ms, DemandType.ArbitraryFeedForward, arbFF);
	}

	@Override
	public void setTargetPosition(double rotations) {
		int ticks = Conversions.fromRotationsToTicks(rotations, ENCODER_CPR);
		_talon.set(ControlMode.Position, ticks);
	}

	@Override
	public void setTargetPosition(double rotations, double arbFF) {
		int ticks = Conversions.fromRotationsToTicks(rotations, ENCODER_CPR);
		_talon.set(ControlMode.Position, ticks, DemandType.ArbitraryFeedForward, arbFF);
	}

	@Override
	public void rezeroSensor() {
		var err = _talon.setSelectedSensorPosition(0);
		notifyOnError(err, "rezeroSensor");
	}

	@Override
	public void setSensorPhase(boolean phase) {
		_talon.setSensorPhase(phase);
	}

	@Override
	public void set(double power) {
		_talon.set(power);
	}

	@Override
	public void setVoltage(double voltage) {
		double compVoltage = getInputVoltage();
		_ctrlMode = ControlMode.PercentOutput;

		var err = _talon.configVoltageCompSaturation(compVoltage);
		_talon.enableVoltageCompensation(true);
		_talon.set(_ctrlMode, voltage / compVoltage);

		notifyOnError(err, "setVoltage_configVoltageCompSaturation");
	}

	@Override
	public double get() {
		return _talon.getMotorOutputPercent();
	}

	@Override
	public void stopMotor() {
		_talon.stopMotor();
	}

	@Override
	public void setInverted(boolean inverted) {
		_talon.setInverted(inverted);
	}

	@Override
	public boolean getInverted() {
		return _talon.getInverted();
	}

	@Override
	public void setPIDF(ClosedLoopConfig closedLoopConfig) {
		var err1 = _talon.config_kP(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkP(), 5);
		var err2 = _talon.config_kI(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkI(), 5);
		var err3 = _talon.config_kD(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkD(), 5);
		var err4 = _talon.config_kF(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkF(), 5);
	
		notifyOnError(err1, "setPIDF (P)");
		notifyOnError(err2, "setPIDF (I)");
		notifyOnError(err3, "setPIDF (D)");
		notifyOnError(err4, "setPIDF (F)");
	}

	@Override
	public boolean getCANConnection() {
		return _talon.getLastError() == ErrorCode.OK;
	}

	@Override
	public SmartMCSim getSim() {
		return _sim;
	}

	@Override
	public void disable() {
		_talon.disable();
	}

	@Override
	public void enableForwardSoftLimit(boolean enable) {
		var err = _talon.configForwardSoftLimitEnable(enable, 5);
		notifyOnError(err, "enableForwardSoftLimit");
	}

	@Override
	public void setForwardSoftLimit(double limit) {
		enableForwardSoftLimit(true);
		int ticks = Conversions.fromRotationsToTicks(limit, ENCODER_CPR);
		var err = _talon.configForwardSoftLimitThreshold(ticks, 5);
		notifyOnError(err, "setForwardSoftLimit");
	}

	@Override
	public void enableReverseSoftLimit(boolean enable) {
		var err = _talon.configReverseSoftLimitEnable(enable, 5);
		notifyOnError(err, "enableReverseSoftLimit");
	}

	@Override
	public void setReverseSoftLimit(double limit) {
		enableReverseSoftLimit(true);
		int ticks = Conversions.fromRotationsToTicks(limit, ENCODER_CPR);
		var err = _talon.configReverseSoftLimitThreshold(ticks, 5);
		notifyOnError(err, "setReverseSoftLimit");
	}

	private void notifyOnError(ErrorCode error, String actionName) {
		if (error == ErrorCode.OK) return;

		String deviceName = "CANTalonSRX (ID: " + _canID + ")";
		String errorStr = deviceName + " | " + actionName + " | ERROR: " + error.name();
		DriverStation.reportError(errorStr, false);
	}
}
