package frc.team832.lib.motorcontrol2.vendor;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANPIDController;
import com.revrobotics.ControlType;
import frc.team832.lib.control.can.CANDeviceManager;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol2.PowerManagedMC;
import frc.team832.lib.motorcontrol2.SmartMC;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.motion.ClosedLoopConfig;
import frc.team832.lib.power.management.PowerManagedDevice;

import java.util.List;

import static com.revrobotics.CANSparkMax.*;

@SuppressWarnings("rawtypes")
public class CANSparkMax implements SmartMC<com.revrobotics.CANSparkMax>, PowerManagedMC {

    private static final List<Motor> allowedMotors = Motor.createAllowedList(Motor.kFalcon500);

    private final com.revrobotics.CANSparkMax _spark;
    private final int _canID;
    private final CANEncoder _encoder;
    private final CANPIDController _pid;
    private final Motor _motor;
    private final PowerManagedDevice _powerManagement;

    private double _openLoopSetpoint;
    private double _closedLoopSetpoint;
    private double _arbFF;


    public CANSparkMax(int canID, Motor motor) {
        assert allowedMotors.contains(motor) : "Invalid motor for CANSparkMax!";

        _motor = motor;
        _canID = canID;

        MotorType motorType = motor == Motor.kNEO || motor == Motor.kNEO550 ? MotorType.kBrushless : MotorType.kBrushed;

        _spark = new com.revrobotics.CANSparkMax(canID, motorType);
        _encoder = _spark.getEncoder();
        _pid = _spark.getPIDController();

        _powerManagement = new PowerManagedDevice() {
            @Override
            public void applyCurrentLimit() {
                CANSparkMax.this.limitInputCurrent((int) getConstrainedCurrentAmps());
            }
        };

        CANDeviceManager.addDevice(this, "Spark MAX");
    }

    @Override
    public Motor getMotor() {
        return _motor;
    }

    @Override
    public void follow(SmartMC masterMC) {
        follow(masterMC, false);
    }

    public void follow(SmartMC masterMC, boolean invert) {
        if (getSafeToCall()) {
            if (masterMC instanceof CANSparkMax) {
                _spark.follow((com.revrobotics.CANSparkMax) masterMC.getBaseController(), invert);
            } else {
                _spark.follow(ExternalFollower.kFollowerPhoenix, masterMC.getCANID(), invert);
            }
        }
    }

    @Override
    public double getInputVoltage() {
        return getSafeToCall() ? _spark.getBusVoltage() : Double.NaN;
    }

    @Override
    public double getOutputVoltage() {
        return getSafeToCall() ? _spark.getAppliedOutput() : Double.NaN;
    }

    @Override
    public double getInputCurrent() {
        return getSafeToCall() ? _spark.getOutputCurrent() : Double.NaN;
    }

    @Override
    public double getOutputCurrent() {
        return getSafeToCall() ? _spark.getOutputCurrent() : Double.NaN;
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        if (getSafeToCall()) {
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
        if (getSafeToCall()) {
            _spark.restoreFactoryDefaults();
        }
    }

    @Override
    public void limitInputCurrent(int currentLimit) {
        if (getSafeToCall()) {
            _spark.setSmartCurrentLimit(currentLimit);
        }
    }

    @Override
    public double getSensorPosition() {
        return getSafeToCall() ? _encoder.getPosition() : Double.NaN;
    }

    @Override
    public double getSensorVelocity() {
        return getSafeToCall() ? _encoder.getVelocity() : Double.NaN;
    }

    @Override
    public void setSensorPhase(boolean phase) {
        if (getSafeToCall()) {
            // Not applicable for SparkMAX in brushless mode
            if (_spark.getMotorType() == MotorType.kBrushed) {
                _encoder.setInverted(phase);
            }
        }
    }

    @Override
    public void rezeroSensor() {
        if (getSafeToCall()) {
            _encoder.setPosition(0);
        }
    }

    public void setTargetVelocity(double target, double arbFF, CANPIDController.ArbFFUnits arbFFUnits) {
        if (getSafeToCall()) {
            if (target != _closedLoopSetpoint || arbFF != _arbFF) {
                _closedLoopSetpoint = target;
                _arbFF = arbFF;
                _pid.setReference(target, ControlType.kVelocity, 0, arbFF, arbFFUnits);
            }
        }
    }

    @Override
    public void setTargetVelocity(double targetRPM, double arbFF) {
        if (getSafeToCall()) {
            if (targetRPM != _closedLoopSetpoint || arbFF != _arbFF) {
                _closedLoopSetpoint = targetRPM;
                _arbFF = arbFF;
                _pid.setReference(targetRPM, ControlType.kVelocity, 0, arbFF, Math.abs(arbFF) > 1 ? CANPIDController.ArbFFUnits.kVoltage : CANPIDController.ArbFFUnits.kPercentOut);
            }
        }
    }

    @Override
    public void setTargetVelocity(double targetRPM) {
        if (getSafeToCall()) {
            if (targetRPM != _closedLoopSetpoint) {
                _closedLoopSetpoint = targetRPM;
                _pid.setReference(targetRPM, ControlType.kVelocity, 0, 0, CANPIDController.ArbFFUnits.kPercentOut);
            }
        }
    }

    @Override
    public void setTargetPosition(double targetPosition, double arbFF) {
        if (targetPosition != _closedLoopSetpoint || arbFF != _arbFF) {
            _closedLoopSetpoint = targetPosition;
            _arbFF = arbFF;

            if (getSafeToCall()) {
                _pid.setReference(targetPosition, ControlType.kPosition, 0, arbFF, Math.abs(arbFF) > 1 ? CANPIDController.ArbFFUnits.kVoltage : CANPIDController.ArbFFUnits.kPercentOut);
            }
        }
    }

    @Override
    public void setTargetPosition(double targetPosition) {
        if (targetPosition != _closedLoopSetpoint) {
            _closedLoopSetpoint = targetPosition;

            if (getSafeToCall()) {
                _pid.setReference(targetPosition, ControlType.kPosition, 0, 0, CANPIDController.ArbFFUnits.kPercentOut);
            }
        }
    }

    @Override
    public void set(double targetThrottle) {
        if (targetThrottle != _openLoopSetpoint) {
            _openLoopSetpoint = targetThrottle;
            if (getSafeToCall()) {
                _spark.set(targetThrottle);
            }
        }
    }

    @Override
    public double get() {
        return getSafeToCall() ? _spark.getAppliedOutput() : Double.NaN;
    }

    @Override
    public void stop() {
        _spark.disable();
    }

    @Override
    public void setInverted(boolean inverted) {
        if (getSafeToCall()) {
            _spark.setInverted(inverted);
        }
    }

    @Override
    public boolean getInverted() {
        return getSafeToCall() && _spark.getInverted();
    }

    @Override
    public com.revrobotics.CANSparkMax getBaseController() {
        return _spark;
    }

    @Override
    public void setPIDF(ClosedLoopConfig closedLoopConfig) {
        if (getSafeToCall()) {
            _pid.setP(closedLoopConfig.getkP());
            _pid.setI(closedLoopConfig.getkI());
            _pid.setD(closedLoopConfig.getkD());
            _pid.setFF(closedLoopConfig.getkF());
        }
    }

    @Override
    public boolean getCANStatus() {
        return _spark.getLastError() == CANError.kOk;
    }

    @Override
    public PowerManagedDevice getPowerManagement() {
        return _powerManagement;
    }
}
