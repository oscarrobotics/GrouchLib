package frc.team832.lib.motorcontrol.vendor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol.base.CANMC;

public class CANVictor implements CANMC {

    private VictorSPX _victor;
    private ControlMode _ctrlMode;

    /***
     * Create an OscarCANTalon at the specified CAN ID.
     * @param canId CAN ID of controller to attach.
     */
    public CANVictor(int canId) {
        _victor = new VictorSPX(canId);
        _ctrlMode = ControlMode.PercentOutput;

        boolean onBus = _victor.getFirmwareVersion() > 0x0102; // TODO: better way to do this?
        CANDevice.addDevice(new CANDevice(canId, onBus, "Victor SPX"));
    }

    @Override
    public void set(double value) {
        _victor.set(_ctrlMode, value);
    }

    public void set(ControlMode mode, double value) {
        _ctrlMode = mode;
        _victor.set(_ctrlMode, value);
    }

    @Override
    public double get() {
        return _victor.getMotorOutputPercent();
    }

    @Override
    public boolean getInverted() {
        return _victor.getInverted();
    }

    @Override
    public void setInverted(boolean isInverted) {
        _victor.setInverted(isInverted);
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        _victor.setNeutralMode(mode == NeutralMode.kBrake ? com.ctre.phoenix.motorcontrol.NeutralMode.Brake : com.ctre.phoenix.motorcontrol.NeutralMode.Coast);
    }

    @Override
    public void disable() {
        _ctrlMode = ControlMode.Disabled;
        set(0);
    }

    @Override
    public void stopMotor() {
        _ctrlMode = ControlMode.PercentOutput;
        set(0);
    }

    public void setMode(ControlMode mode) {
        _ctrlMode = mode;
    }

    @Override
    public void follow(int masterMotorID) {
        _ctrlMode = ControlMode.Follower;
        set((double) masterMotorID);
    }

    public void follow(CANMC motor) {
        int id32 = motor.getBaseID();
        int id24 = id32;
        id24 >>= 16;
        id24 = (short) id24;
        id24 <<= 8;
        id24 |= (id32 & 0xFF);
        System.out.println("Slave motor " + getDeviceID() + " following " + id24);
        follow(id24);
    }

    public double getInputVoltage() {
        return _victor.getBusVoltage();
    }

    public double getOutputVoltage() {
        return _victor.getMotorOutputVoltage();
    }

    @Override
    public double getOutputCurrent() {
        return 0;
    }

    @Override
    public void resetSettings() {
        _victor.configFactoryDefault();
    }

    @Override
    public int getDeviceID() {
        return _victor.getDeviceID();
    }

    @Override
    public int getBaseID() {
        return _victor.getBaseID();
    }
}
