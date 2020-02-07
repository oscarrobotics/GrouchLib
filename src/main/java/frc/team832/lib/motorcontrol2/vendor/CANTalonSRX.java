package frc.team832.lib.motorcontrol2.vendor;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol2.SmartMC;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.util.ClosedLoopConfig;

public class CANTalonSRX implements SmartMC<TalonSRX> {
    private final TalonSRX _talon;

    private final Motor _motor;
    private final int _canID;

    private boolean _onBus;

    private ControlMode _ctrlMode;
    private SupplyCurrentLimitConfiguration inputCurrentConfig = new SupplyCurrentLimitConfiguration(true, 40, 0, 0);


    /***
     * Create an OscarCANTalon at the specified CAN ID.
     * @param canId CAN ID of controller to attach.
     */
    public CANTalonSRX(int canId, Motor motor) {
        assert motor != Motor.kNEO && motor != Motor.kNEO550 && motor != Motor.kFalcon500 : "Invalid motor for CANTalonSRX!";

        _motor = motor;
        _talon = new TalonSRX(canId);
        _canID = canId;
        _ctrlMode = ControlMode.PercentOutput;

        _onBus = _talon.getBusVoltage() > 0.0; // TODO: better way to do this?
        CANDevice.addDevice(new CANDevice(_canID, _onBus, "Talon SRX"));
    }

    @Override
    public Motor getMotor() {
        return _motor;
    }

    @Override
    public TalonSRX getBaseController() {
        return _talon;
    }

    @Override
    public void follow(SmartMC masterMC) {
        if (!(masterMC instanceof CANSparkMax)) {
            _talon.follow(((CANTalonSRX)masterMC).getBaseController());
        } else {
            _ctrlMode = ControlMode.Follower;
            _talon.set(_ctrlMode, masterMC.getCANID());
        }
    }

    @Override
    public double getInputVoltage() {
        return _onBus ? _talon.getBusVoltage() : Double.NaN;
    }

    @Override
    public double getOutputVoltage() {
        return _onBus ? _talon.getMotorOutputVoltage() : Double.NaN;
    }

    @Override
    public double getInputCurrent() {
        return _onBus ? _talon.getSupplyCurrent() : Double.NaN;
    }

    @Override
    public double getOutputCurrent() {
        return _onBus ? _talon.getStatorCurrent() : Double.NaN;
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        if (_onBus) {
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
        if (_onBus) {
            _talon.configFactoryDefault();
        }
    }

    @Override
    public void limitInputCurrent(int currentLimit) {
        if (_onBus) {
            inputCurrentConfig.currentLimit = currentLimit;
            _talon.configSupplyCurrentLimit(inputCurrentConfig);
        }
    }

    @Override
    public double getSensorPosition() {
        return _onBus ? (_talon.getSelectedSensorPosition() / 4096.0) : Double.NaN;
    }

    @Override
    public double getSensorVelocity() {
        return _onBus ? ((_talon.getSelectedSensorVelocity() / 4096.0) * 600) : Double.NaN;
    }

    @Override
    public void setVelocity(double v) {
        if (_onBus) {
            _talon.configMotionCruiseVelocity((int) v);
        }
    }

    @Override
    public void rezeroSensor() {
        if (_onBus) {
            _talon.setSelectedSensorPosition(0);
        }
    }

    @Override
    public void setEncoderPosition(double position) {
        if (_onBus) {
            _talon.setSelectedSensorPosition((int) position);
        }
    }

    @Override
    public void setSensorPhase(boolean phase) {
        if (_onBus) {
            _talon.setSensorPhase(phase);
        }
    }

    @Override
    public void set(double power) {
        if (_onBus) {
            _ctrlMode = ControlMode.PercentOutput;
            _talon.set(_ctrlMode, power);
        }
    }

    @Override
    public double get() {
        return _onBus ? _talon.getMotorOutputPercent() : Double.NaN;
    }

    @Override
    public void stop() {
        if (_onBus) {
            _talon.set(ControlMode.PercentOutput, 0);
        }
    }

    @Override
    public void setInverted(boolean inverted) {
        if (_onBus) {
            _talon.setInverted(inverted);
        }
    }

    @Override
    public boolean getInverted() {
        return _onBus && _talon.getInverted();
    }

    @Override
    public void setPIDF(ClosedLoopConfig closedLoopConfig) {
        if (_onBus) {
            _talon.config_kP(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkP());
            _talon.config_kI(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkI());
            _talon.config_kD(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkD());
            _talon.config_kF(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkF());
        }
    }
}
