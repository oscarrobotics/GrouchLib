package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class OscarCANVictor implements IOscarSmartMotor {

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
    public int getPosition() {
        return 0; // Not supported by Victor
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

    @Override
    public void setMode(ControlMode mode) {
        _ctrlMode = mode;
    }

    @Override
    public void follow(int masterMotorID) {
        _ctrlMode = ControlMode.Follower;
        set((double) masterMotorID);
    }

    public void follow(IOscarSmartMotor motor) {
        int id32 = motor.getBaseID();
        int id24 = id32;
        id24 >>= 16;
        id24 = (short) id24;
        id24 <<= 8;
        id24 |= (id32 & 0xFF);
        System.out.println("Slave motor " + getDeviceID() + " following " + id24);
        follow(id24);
    }

    @Override
    public double getInputVoltage() {
        return _victor.getBusVoltage();
    }

    @Override
    public double getOutputVoltage() {
        return _victor.getMotorOutputVoltage();
    }

    @Override
    public double getOutputCurrent() {
        return 0; // Not supported by Victor.
    }

    @Override
    public int getDeviceID() {
        return _victor.getDeviceID();
    }

    @Override
    public int getBaseID() {
        return _victor.getBaseID();
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        _victor.setNeutralMode(mode);
    }

    @Override
    public void setSensorPhase(boolean phase) {
        // Not supported by Victor
    }

    @Override
    public void setSensor(FeedbackDevice device) {
        // Not supported by Victor
    }

    @Override
    public void setAllowableClosedLoopError(int error) {
        // Not supported by Victor
    }

    @Override
    public void setClosedLoopRamp(double secondsFromNeutralToFull) {
        // Not supported by Victor
    }

    @Override
    public void setOpenLoopRamp(double secondsFromNeutralToFull) {
        // Not supported by Victor
    }

    @Override
    public int getSensorPosition() {
        return 0; // Not supported by Victor
    }

    @Override
    public void setSensorPosition(int absolutePosition) {
        // Not supported by Victor
    }

    @Override
    public double getClosedLoopTarget() {
        return 0; // Not supported by Victor
    }

    @Override
    public boolean getForwardLimitSwitch() {
        return false; // Not supported by Victor
    }

    @Override
    public boolean getReverseLimitSwitch() {
        return false; // Not supported by Victor
    }

    @Override
    public int getClosedLoopError() {
        return 0; // Not supported by Victor
    }

    @Override
    public int getPulseWidthPosition() {
        return 0; // Not supported by Victor
    }

    @Override
    public void set_kF(int slot, double kF) {
        // Not supported by Victor
    }

    @Override
    public void setPeakOutputForward(double percentOut) {
        // Not supported by Victor
    }

    @Override
    public void setPeakOutputReverse(double percentOut) {
        // Not supported by Victor
    }

    @Override
    public void setNominalOutputForward(double percentOut) {
        // Not supported by Victor
    }

    @Override
    public void setNominalOutputReverse(double percentOut) {

    }
}
