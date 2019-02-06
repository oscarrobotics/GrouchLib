package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class OscarCANVictor implements IOscarCANMotor {

    private VictorSPX _victor;
    private ControlMode _ctrlMode;

    /***
     * Create an OscarCANTalon at the specified CAN ID.
     * @param canId CAN ID of controller to attach.
     */
    public OscarCANVictor(int canId) {
        _victor = new VictorSPX(canId);
        _ctrlMode = ControlMode.Disabled;
        set(0);
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

    public void follow(IOscarCANMotor motor) {
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
    public int getDeviceID() {
        return _victor.getDeviceID();
    }

    @Override
    public int getBaseID() {
        return _victor.getBaseID();
    }

    public void setNeutralMode(NeutralMode mode) {
        _victor.setNeutralMode(mode);
    }


}
