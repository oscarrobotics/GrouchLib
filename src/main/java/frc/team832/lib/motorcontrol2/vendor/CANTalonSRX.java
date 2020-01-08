package frc.team832.lib.motorcontrol2.vendor;

import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol2.PowerManagedMC;
import frc.team832.lib.motorcontrol2.SmartMC;

public class CANTalonSRX extends PowerManagedMC<TalonSRX> {
    
    private TalonSRX _talon;
    private ControlMode _ctrlMode;
    private double _allowableError = 0;
    private double _openLoopSetpoint;

    private SupplyCurrentLimitConfiguration inputCurrentConfig = new SupplyCurrentLimitConfiguration(true, 40, 0, 0);
    private StatorCurrentLimitConfiguration outputCurrentConfig = new StatorCurrentLimitConfiguration(true, 40, 0, 0);


    /***
     * Create an OscarCANTalon at the specified CAN ID.
     * @param canId CAN ID of controller to attach.
     */
    public CANTalonSRX(int canId) {
        _talon = new TalonSRX(canId);
        _ctrlMode = ControlMode.Disabled;

        boolean onBus = _talon.getBusVoltage() > 0.0; // TODO: better way to do this?
        CANDevice.addDevice(new CANDevice(canId, onBus, "Talon SRX"));
    }

    @Override
    public void follow(SmartMC masterMC) {
        _ctrlMode = ControlMode.Follower;
        _talon.set(_ctrlMode, masterMC.getCANID());
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
        _talon.setNeutralMode(mode == NeutralMode.kBrake ?
         com.ctre.phoenix.motorcontrol.NeutralMode.Brake :
          com.ctre.phoenix.motorcontrol.NeutralMode.Coast);
    }

    @Override
    public int getCANID() {
        return _talon.getBaseID();
    }

    @Override
    public void wipeSettings () {
        _talon.configFactoryDefault();
    }

    @Override
    public void limitInputCurrent(int currentLimit) {
        inputCurrentConfig.currentLimit = currentLimit;
        _talon.configSupplyCurrentLimit(inputCurrentConfig);
    }

    @Override
    public double getSensorPosition () {
        return _talon.getSelectedSensorPosition();
    }

    @Override
    public double getSensorVelocity () {
        return _talon.getSelectedSensorVelocity();
    }

    @Override
    public void setVelocity (double v) {
        _talon.configMotionCruiseVelocity((int) v);
    }

    @Override
    public void set(double power) {
        _talon.set(_ctrlMode, power);
    }

    @Override
    public double get() {
        return _talon.getMotorOutputPercent();
    }

    @Override
    public void stop() {
        _talon.set(ControlMode.PercentOutput, 0);
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
    public TalonSRX getBaseController() {
        return _talon;
    }

    @Override
    public void stopMotor () {

    }
}
