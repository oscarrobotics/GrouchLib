package frc.team832.lib.motorcontrol.vendor;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.util.ClosedLoopConfig;

public class CANTalonFX implements SmartMC<WPI_TalonFX, CANTalonFXSimCollection> {

	private final WPI_TalonFX _talon;
	private final int _canID;
	private final CANTalonFXSimCollection _simCollection;

	private ControlMode _ctrlMode;
	private SupplyCurrentLimitConfiguration inputCurrentConfig = new SupplyCurrentLimitConfiguration(true, 40, 0, 0);
	private StatorCurrentLimitConfiguration outputCurrentConfig = new StatorCurrentLimitConfiguration(true, 40, 0, 0);

	private final boolean canConnectedAtBoot;

	public CANTalonFX(int canId) {
		_talon = new WPI_TalonFX(canId);
		_canID = canId;
		_ctrlMode = ControlMode.Disabled;

		_simCollection = new CANTalonFXSimCollection(this);

		canConnectedAtBoot = getCANConnection();
		
		CANDevice.addDevice(this, "Talon FX");
	}

	@Override
	public Motor getMotor() {
		return Motor.kFalcon500;
	}

	@Override
	public WPI_TalonFX getBaseController() {
		return _talon;
	}

	@Override
	public void follow(WPI_TalonFX master) {
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
		// if (canConnectedAtBoot) {
			_talon.setNeutralMode(mode == NeutralMode.kBrake ?
					com.ctre.phoenix.motorcontrol.NeutralMode.Brake :
					com.ctre.phoenix.motorcontrol.NeutralMode.Coast);
		// }
	}

	@Override
	public int getCANID() {
		return _canID;
	}

	@Override
	public void wipeSettings() {
		if (canConnectedAtBoot) {
			_talon.configFactoryDefault();
		}
	}

	@Override
	public void limitInputCurrent(int currentLimit) {
		if (canConnectedAtBoot) {
			inputCurrentConfig.currentLimit = currentLimit;
			_talon.configSupplyCurrentLimit(inputCurrentConfig);
		}
	}

	public void limitOutputCurrent(int currentLimit) {
		if (canConnectedAtBoot) {
			outputCurrentConfig.currentLimit = currentLimit;
			_talon.configStatorCurrentLimit(outputCurrentConfig);
		}
	}

	@Override
	public double getSensorPosition() {
		return canConnectedAtBoot ? (_talon.getSelectedSensorPosition() / 2048.0) : Double.NaN;
	}

	@Override
	public double getSensorVelocity() {
		if (canConnectedAtBoot) {
			return (_talon.getSelectedSensorVelocity() / 2048.0) * 600;
		} else return Double.NaN;
	}

	@Override
	public void setMotionProfileVelocity(double velocityPerSec) {
		if (canConnectedAtBoot) {
			_talon.configMotionCruiseVelocity((int) (velocityPerSec / 10));
		}
	}

	@Override
	public void setTargetVelocity(double target) {
		if (canConnectedAtBoot) {
			_talon.set(ControlMode.Velocity, target);
		}
	}

	@Override
	public void setTargetVelocity(double target, double arbFF) {
		if (canConnectedAtBoot) {
			_talon.set(ControlMode.Velocity, target, DemandType.ArbitraryFeedForward, arbFF);
		}
	}

	@Override
	public void setTargetPosition(double target) {
		if (canConnectedAtBoot) {
			_talon.set(ControlMode.Position, target);
		}
	}

	@Override
	public void setTargetPosition(double target, double arbFF) {
		if (canConnectedAtBoot) {
			_talon.set(ControlMode.Position, target, DemandType.ArbitraryFeedForward, arbFF);
		}
	}

	@Override
	public void rezeroSensor() {
		if (canConnectedAtBoot) {
			_talon.setSelectedSensorPosition(0);
		}
	}

	@Override
	public void setSensorPhase(boolean phase) { }

	@Override
	public void set(double power) {
		_talon.set(power);
	}

	@Override
	public void setVoltage(double voltage) {
		double compVoltage = Math.max(12.0, getInputVoltage());
		_ctrlMode = ControlMode.PercentOutput;

		_talon.configVoltageCompSaturation(compVoltage);
		_talon.enableVoltageCompensation(true);
		_talon.set(_ctrlMode, voltage / compVoltage);
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
		if (canConnectedAtBoot) {
			_talon.config_kP(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkP());
			_talon.config_kI(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkI());
			_talon.config_kD(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkD());
			_talon.config_kF(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkF());
		}
	}

	@Override
	public boolean getCANConnection() {
		return _talon.getLastError() == ErrorCode.OK;
	}

	@Override
	public CANTalonFXSimCollection getSimCollection() {
		return _simCollection;
	}
		
	@Override
	public void disable() {
		_talon.disable();
	}
}
