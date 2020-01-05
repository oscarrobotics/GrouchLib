package frc.team832.lib.motorcontrol2.vendor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol2.PowerManagedMC;
import frc.team832.lib.motorcontrol2.SmartMC;

public class CANTalonFX extends PowerManagedMC<TalonFX> {

    private TalonFX _talon;
    private ControlMode _ctrlMode;

    public CANTalonFX(int canId){
        _talon = new TalonFX(canId);
        _ctrlMode = ControlMode.Disabled;

        boolean onBus = _talon.getBusVoltage() > 0.0; // TODO: better way to do this?
        CANDevice.addDevice(new CANDevice(canId, onBus, "Talon FX"));
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
        return 0;
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
    public TalonFX getBaseController() {
        return _talon;
    }
}