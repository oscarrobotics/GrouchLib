package frc.team832.lib.motorcontrol2.vendor;

import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.RobotBase;
import frc.team832.lib.control.can.CANDeviceManager;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol2.SmartMC;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.motion.ClosedLoopConfig;

import java.util.List;

@SuppressWarnings("rawtypes")
public class CANTalonSRX implements SmartMC<TalonSRX> {

    private static final List<Motor> allowedMotors = Motor.createAllowedList(Motor.kNEO, Motor.kNEO550, Motor.kFalcon500);

    private final TalonSRX _talon;
    private final Motor _motor;
    private final int _canID;

    private ControlMode _ctrlMode;
    private SupplyCurrentLimitConfiguration inputCurrentConfig = new SupplyCurrentLimitConfiguration(true, 40, 0, 0);

    public CANTalonSRX(int canId, Motor motor) {
        assert allowedMotors.contains(motor) : "Invalid motor for CANTalonSRX!";

        _motor = motor;
        _talon = new TalonSRX(canId);
        _canID = canId;
        _ctrlMode = ControlMode.PercentOutput;

        CANDeviceManager.addDevice(this, "Talon SRX");
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
        return getCANStatus() ? _talon.getBusVoltage() : Double.NaN;
    }

    @Override
    public double getOutputVoltage() {
        return getCANStatus() ? _talon.getMotorOutputVoltage() : Double.NaN;
    }

    @Override
    public double getInputCurrent() {
        return getCANStatus() ? _talon.getSupplyCurrent() : Double.NaN;
    }

    @Override
    public double getOutputCurrent() {
        return getCANStatus() ? _talon.getStatorCurrent() : Double.NaN;
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        if (getCANStatus()) {
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
        if (getCANStatus()) {
            _talon.configFactoryDefault();
        }
    }

    @Override
    public void limitInputCurrent(int currentLimit) {
        if (getCANStatus()) {
            inputCurrentConfig.currentLimit = currentLimit;
            _talon.configSupplyCurrentLimit(inputCurrentConfig);
        }
    }

    @Override
    public double getSensorPosition() {
        return getCANStatus() ? (_talon.getSelectedSensorPosition() / 4096.0) : Double.NaN;
    }

    @Override
    public double getSensorVelocity() {
        return getCANStatus() ? ((_talon.getSelectedSensorVelocity() / 4096.0) * 600) : Double.NaN;
    }

    @Override
    public void setTargetVelocity(double target) {
        if (getCANStatus()) {
            _talon.set(ControlMode.Velocity, target);
        }
    }

    @Override
    public void setTargetVelocity(double target, double arbFF) {
        if (getCANStatus()) {
            _talon.set(ControlMode.Velocity, target, DemandType.ArbitraryFeedForward, arbFF);
        }
    }

    @Override
    public void setTargetPosition(double target) {
        if (getCANStatus()) {
            _talon.set(ControlMode.Position, target);
        }
    }

    @Override
    public void setTargetPosition(double target, double arbFF) {
        if (getCANStatus()) {
            _talon.set(ControlMode.Position, target, DemandType.ArbitraryFeedForward, arbFF);
        }
    }

    @Override
    public void rezeroSensor() {
        if (getCANStatus()) {
            _talon.setSelectedSensorPosition(0);
        }
    }

    @Override
    public void setSensorPhase(boolean phase) {
        if (getCANStatus()) {
            _talon.setSensorPhase(phase);
        }
    }

    @Override
    public void set(double power) {
        if (getCANStatus()) {
            _ctrlMode = ControlMode.PercentOutput;
            _talon.set(_ctrlMode, power);
        }
    }

    @Override
    public double get() {
        return getCANStatus() ? _talon.getMotorOutputPercent() : Double.NaN;
    }

    @Override
    public void stop() {
        if (getCANStatus()) {
            _talon.set(ControlMode.PercentOutput, 0);
        }
    }

    @Override
    public void setInverted(boolean inverted) {
        if (getCANStatus()) {
            _talon.setInverted(inverted);
        }
    }

    @Override
    public boolean getInverted() {
        return getCANStatus() && _talon.getInverted();
    }

    @Override
    public void setPIDF(ClosedLoopConfig closedLoopConfig) {
        if (getCANStatus()) {
            _talon.config_kP(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkP());
            _talon.config_kI(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkI());
            _talon.config_kD(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkD());
            _talon.config_kF(closedLoopConfig.getSlotIDx(), closedLoopConfig.getkF());
        }
    }

    @Override
    public boolean getCANStatus() {
        return RobotBase.isReal() && _talon.getBusVoltage() > 0.0;
    }
}
