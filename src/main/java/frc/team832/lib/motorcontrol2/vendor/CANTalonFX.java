package frc.team832.lib.motorcontrol2.vendor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import frc.team832.lib.control.can.CANDeviceManager;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol2.SmartMC;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.motion.ClosedLoopConfig;

@SuppressWarnings({"unused", "rawtypes"})
public class CANTalonFX implements SmartMC<TalonFX> {

    private final TalonFX _talon;
    private final int _canID;

    private ControlMode _ctrlMode;
    private SupplyCurrentLimitConfiguration inputCurrentConfig = new SupplyCurrentLimitConfiguration(true, 40, 0, 0);
    private StatorCurrentLimitConfiguration outputCurrentConfig = new StatorCurrentLimitConfiguration(true, 40, 0, 0);

    private final boolean canConnectedAtBoot;    

    public CANTalonFX(int canId) {
        _talon = new TalonFX(canId);
        _canID = canId;
        _ctrlMode = ControlMode.Disabled;

        // dummy call to set CAN ErrorCode
        _talon.getBusVoltage();

        canConnectedAtBoot = getCANStatus();
        
        CANDeviceManager.addDevice(this, "Talon FX");
    }

    @Override
    public Motor getMotor() {
        return Motor.kFalcon500;
    }

    @Override
    public TalonFX getBaseController() {
        return _talon;
    }

    @Override
    public void follow(SmartMC masterMC) {
        if (getSafeToCall()) {
			_ctrlMode = ControlMode.Follower;
			if (masterMC instanceof CANTalonFX) {
				_talon.follow(((CANTalonFX) masterMC).getBaseController());
			} else if (masterMC instanceof CANTalonSRX) {
				_talon.follow(((CANTalonSRX) masterMC).getBaseController());
        	} else {
	            _talon.set(_ctrlMode, masterMC.getCANID());
        	}
        }
    }

    @Override
    public double getInputVoltage() {
        return canConnectedAtBoot ? _talon.getBusVoltage() : 0.0;
    }

    @Override
    public double getOutputVoltage() {
        return canConnectedAtBoot ? _talon.getMotorOutputVoltage() : 0.0;
    }

    @Override
    public double getInputCurrent() {
        return canConnectedAtBoot ? _talon.getSupplyCurrent() : 0.0;
    }

    @Override
    public double getOutputCurrent() {
        return canConnectedAtBoot ? _talon.getStatorCurrent() : 0.0;
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        if (getSafeToCall()) {
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
        if (getSafeToCall()) {
            _talon.configFactoryDefault();
        }
    }

    @Override
    public void limitInputCurrent(int currentLimit) {
        if (getSafeToCall()) {
            inputCurrentConfig.currentLimit = currentLimit;
            _talon.configSupplyCurrentLimit(inputCurrentConfig);
        }
    }

    public void limitOutputCurrent(int currentLimit) {
        if (getSafeToCall()) {
            outputCurrentConfig.currentLimit = currentLimit;
            _talon.configStatorCurrentLimit(outputCurrentConfig);
        }
    }

    @Override
    public double getSensorPosition() {
        return canConnectedAtBoot ? (_talon.getSelectedSensorPosition() / 2048.0) : 0.0;
    }

    @Override
    public double getSensorVelocity() {
        if (getSafeToCall()) {
            return (_talon.getSelectedSensorVelocity() / 2048.0) * 600;
        } else return 0.0;
    }

    @Override
    public void setTargetVelocity(double target) {
        if (getSafeToCall()) {
            _talon.set(ControlMode.Velocity, target);
        }
    }

    @Override
    public void setTargetVelocity(double target, double arbFF) {
        if (getSafeToCall()) {
            _talon.set(ControlMode.Velocity, target, DemandType.ArbitraryFeedForward, arbFF);
        }
    }

    @Override
    public void setTargetPosition(double target) {
        if (getSafeToCall()) {
            _talon.set(ControlMode.Position, target);
        }
    }

    @Override
    public void setTargetPosition(double target, double arbFF) {
        if (getSafeToCall()) {
            _talon.set(ControlMode.Position, target, DemandType.ArbitraryFeedForward, arbFF);
        }
    }

    @Override
    public void rezeroSensor() {
        if (getSafeToCall()) {
            _talon.setSelectedSensorPosition(0);
        }
    }

    @Override
    public void setSensorPhase(boolean phase) { }

    @Override
    public void set(double power) {
        if (getSafeToCall()) {
			_ctrlMode = ControlMode.PercentOutput;
            _talon.set(_ctrlMode, power);
        }
    }

    @Override
    public double get() {
        return canConnectedAtBoot ? _talon.getMotorOutputPercent() : 0.0;
    }

    @Override
    public void stop() {
        if (getSafeToCall()) {
            _talon.set(ControlMode.PercentOutput, 0);
        }
    }

    @Override
    public void setInverted(boolean inverted) {
        if (getSafeToCall()) {
            _talon.setInverted(inverted);
        }
    }

    @Override
    public boolean getInverted() {
        return canConnectedAtBoot && _talon.getInverted();
    }

    @Override
    public void setPIDF(ClosedLoopConfig closedLoopConfig) {
        if (getSafeToCall()) {
            _talon.config_kP(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkP());
            _talon.config_kI(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkI());
            _talon.config_kD(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkD());
            _talon.config_kF(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkF());
        }
    }

    @Override
    public boolean getCANStatus() {
        return _talon.getBusVoltage() > 0.0;
    }
}
