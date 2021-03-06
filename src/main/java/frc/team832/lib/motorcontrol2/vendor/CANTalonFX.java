package frc.team832.lib.motorcontrol2.vendor;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol2.SmartMC;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.util.ClosedLoopConfig;

@SuppressWarnings("unused")
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

        canConnectedAtBoot = getCANConnection();
        
        CANDevice.addDevice(this, "Talon FX");
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
        if (canConnectedAtBoot) {
			_ctrlMode = ControlMode.Follower;
			if (masterMC instanceof  CANTalonFX) {
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
        return canConnectedAtBoot ? _talon.getBusVoltage() : Double.NaN;
    }

    @Override
    public double getOutputVoltage() {
        return canConnectedAtBoot ? _talon.getMotorOutputVoltage() : Double.NaN;
    }

    @Override
    public double getInputCurrent() {
        return canConnectedAtBoot ? _talon.getSupplyCurrent() : Double.NaN;
    }

    @Override
    public double getOutputCurrent() {
        return canConnectedAtBoot ? _talon.getStatorCurrent() : Double.NaN;
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        if (canConnectedAtBoot) {
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
        if (canConnectedAtBoot) {
			_ctrlMode = ControlMode.PercentOutput;
            _talon.set(_ctrlMode, power);
        }
    }

    @Override
    public double get() {
        return canConnectedAtBoot ? _talon.getMotorOutputPercent() : Double.NaN;
    }

    @Override
    public void stop() {
        if (canConnectedAtBoot) {
            _talon.set(ControlMode.PercentOutput, 0);
        }
    }

    @Override
    public void setInverted(boolean inverted) {
        if (canConnectedAtBoot) {
            _talon.setInverted(inverted);
        }
    }

    @Override
    public boolean getInverted() {
        return canConnectedAtBoot && _talon.getInverted();
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
}
