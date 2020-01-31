package frc.team832.lib.motorcontrol2.vendor;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol2.PowerManagedMC;
import frc.team832.lib.motorcontrol2.SmartMC;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.util.ClosedLoopConfig;

import static com.revrobotics.CANSparkMax.*;

public class CANSparkMax extends PowerManagedMC<com.revrobotics.CANSparkMax> {

    private final com.revrobotics.CANSparkMax _spark;
    private final CANEncoder _encoder;
    private final CANPIDController _pid;
    private final Motor _motor;

    public CANSparkMax(int canId, Motor motor) {
        assert motor != Motor.kFalcon500 : "Invalid motor for CANSparkMax!";

        _motor = motor;

        MotorType motorType = motor == Motor.kNEO || motor == Motor.kNEO550 ? MotorType.kBrushless : MotorType.kBrushed;

        _spark = new com.revrobotics.CANSparkMax(canId, motorType);
        _encoder = _spark.getEncoder();
        _pid = _spark.getPIDController();

        boolean onBus = !_spark.getFirmwareString().equals("");
        CANDevice.addDevice(new CANDevice(canId, onBus, "SparkMax"));
    }

    @Override
    public Motor getMotor() {
        return _motor;
    }

    @Override
    public void follow(SmartMC masterMC) {
        if (masterMC instanceof CANSparkMax) {
            _spark.follow((com.revrobotics.CANSparkMax)masterMC.getBaseController());
        } else {
            _spark.follow(ExternalFollower.kFollowerPhoenix, masterMC.getCANID());
        }
    }

    @Override
    public double getInputVoltage() {
        return _spark.getBusVoltage();
    }

    @Override
    public double getOutputVoltage() {
        return _spark.getAppliedOutput();
    }

    @Override
    public double getInputCurrent() {
        return _spark.getOutputCurrent(); // todo: any way to do this?
    }

    @Override
    public double getOutputCurrent() {
        return _spark.getOutputCurrent();
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        _spark.setIdleMode(mode == NeutralMode.kBrake ?
                IdleMode.kBrake :
                IdleMode.kCoast);
    }

    @Override
    public int getCANID() {
        return _spark.getDeviceId();
    }

    @Override
    public void wipeSettings() {
        _spark.restoreFactoryDefaults();
    }

    @Override
    public void limitInputCurrent(int currentLimit) {
        _spark.setSmartCurrentLimit(currentLimit);
    }

    @Override
    public double getSensorPosition() {
        return _encoder.getPosition();
    }

    @Override
    public double getSensorVelocity() {
        return _encoder.getVelocity();
    }

    @Override
    public void setVelocity(double v) {
        _pid.setSmartMotionMaxVelocity(v, _spark.getDeviceId());
    }

    public void setEncoderPosition(double pos){ _pid.setReference(pos, ControlType.kPosition);}

    @Override
    public void setSensorPhase(boolean phase) { }

    @Override
    public void rezeroSensor() { _encoder.setPosition(0); }

    @Override
    public void set(double power) { _spark.set(power); }

    @Override
    public double get() { return _spark.getAppliedOutput(); }

    @Override
    public void stop() { _spark.set(0); }

    @Override
    public void setInverted(boolean inverted) { _spark.setInverted(inverted); }

    @Override
    public boolean getInverted() { return _spark.getInverted(); }

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
