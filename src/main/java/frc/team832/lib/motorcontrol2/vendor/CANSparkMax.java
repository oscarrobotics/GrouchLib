package frc.team832.lib.motorcontrol2.vendor;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;
import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol2.SmartMC;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.util.ClosedLoopConfig;

import static com.revrobotics.CANSparkMax.*;

public class CANSparkMax implements SmartMC<com.revrobotics.CANSparkMax> {

    private final com.revrobotics.CANSparkMax _spark;
    private final int _canID;
    private final CANEncoder _encoder;
    private final CANPIDController _pid;
    private final Motor _motor;

    private boolean _onBus;

    public CANSparkMax(int canID, Motor motor) {
        assert motor != Motor.kFalcon500 : "Invalid motor for CANSparkMax!";

        _motor = motor;
        _canID = canID;

        MotorType motorType = motor == Motor.kNEO || motor == Motor.kNEO550 ? MotorType.kBrushless : MotorType.kBrushed;

        _spark = new com.revrobotics.CANSparkMax(canID, motorType);
        _encoder = _spark.getEncoder();
        _pid = _spark.getPIDController();

        _onBus = !_spark.getFirmwareString().equals("");
        CANDevice.addDevice(new CANDevice(canID, _onBus, "SparkMax"));
    }

    @Override
    public Motor getMotor() {
        return _motor;
    }

    @Override
    public void follow(SmartMC masterMC) {
        if (_onBus) {
            if (masterMC instanceof CANSparkMax) {
                _spark.follow((com.revrobotics.CANSparkMax) masterMC.getBaseController());
            } else {
                _spark.follow(ExternalFollower.kFollowerPhoenix, masterMC.getCANID());
            }
        }
    }

    public void follow(SmartMC masterMC, boolean invert) {
        if (_onBus) {
            if (masterMC instanceof CANSparkMax) {
                _spark.follow((com.revrobotics.CANSparkMax) masterMC.getBaseController(), invert);
            } else {
                _spark.follow(ExternalFollower.kFollowerPhoenix, masterMC.getCANID(), invert);
            }
        }
    }

    @Override
    public double getInputVoltage() {
        return _onBus ? _spark.getBusVoltage() : Double.NaN;
    }

    @Override
    public double getOutputVoltage() {
        return _onBus ? _spark.getAppliedOutput() : Double.NaN;
    }

    @Override
    public double getInputCurrent() {
        return _onBus ? _spark.getOutputCurrent() : Double.NaN; // todo: any way to do this?
    }

    @Override
    public double getOutputCurrent() {
        return _onBus ? _spark.getOutputCurrent() : Double.NaN;
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        if (_onBus) {
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
        if (_onBus) {
            _spark.restoreFactoryDefaults();
        }
    }

    @Override
    public void limitInputCurrent(int currentLimit) {
        if (_onBus) {
            _spark.setSmartCurrentLimit(currentLimit);
        }
    }

    @Override
    public double getSensorPosition() {
        return _onBus ? _encoder.getPosition() : Double.NaN;
    }

    @Override
    public double getSensorVelocity() {
        return _onBus ? _encoder.getVelocity() : Double.NaN;
    }

    @Override
    public void setVelocity(double v) {
        if (_onBus) {
            _pid.setSmartMotionMaxVelocity(v, _spark.getDeviceId());
        }
    }

    public void setEncoderPosition(double pos) {
        if (_onBus) {
            _pid.setReference(pos, ControlType.kPosition);
        }
    }

    @Override
    public void setSensorPhase(boolean phase) { }

    @Override
    public void rezeroSensor() {
        if (_onBus) {
            _encoder.setPosition(0);
        }
    }

    @Override
    public void set(double power) {
        if (_onBus) {
            _spark.set(power);
        }
    }

    @Override
    public double get() {
        return _onBus ? _spark.getAppliedOutput() : Double.NaN;
    }

    @Override
    public void stop() {
        if (_onBus) {
            _spark.set(0);
        }
    }

    @Override
    public void setInverted(boolean inverted) {
        if (_onBus) {
            _spark.setInverted(inverted);
        }
    }

    @Override
    public boolean getInverted() {
        return _onBus && _spark.getInverted();
    }

    @Override
    public com.revrobotics.CANSparkMax getBaseController() { return _spark; }

    @Override
    public void setPIDF(ClosedLoopConfig closedLoopConfig) {
        _pid.setP(closedLoopConfig.getkP());
        _pid.setI(closedLoopConfig.getkI());
        _pid.setD(closedLoopConfig.getkD());
        _pid.setFF(closedLoopConfig.getkF());
    }
}
