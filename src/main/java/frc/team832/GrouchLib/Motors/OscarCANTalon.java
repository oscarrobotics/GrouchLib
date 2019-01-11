package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * Implementation of IOscarSmartMotor that is specific to a CTRE Talon SRX
 */
public class OscarCANTalon implements IOscarSmartMotor {

    private TalonSRX _talon;
    private ControlMode _ctrlMode;
    private int _curPidIdx = 0;

    /***
     * Create an OscarCANTalon at the specified CAN ID.
     * @param canId CAN ID of controller to attach.
     */
    public OscarCANTalon(int canId) {
        _talon = new TalonSRX(canId);
        _ctrlMode = ControlMode.Disabled;
        set(0);
    }

    @Override
    public void set(double value) {
        _talon.set(_ctrlMode, value);
    }

    public void set(ControlMode ctrlMode, double value) {
        _ctrlMode = ctrlMode;
        _talon.set(_ctrlMode, value);
    }

    @Override
    public double get() {
        return _talon.getMotorOutputPercent();
    }

    @Override
    public int getPosition() {
        return _talon.getSelectedSensorPosition(_curPidIdx);
    }

    @Override
    public boolean getInverted() {
        return _talon.getInverted();
    }

    @Override
    public void setInverted(boolean isInverted) {
        _talon.setInverted(isInverted);
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
        set(masterMotorID);
    }

    @Override
    public void follow(IOscarSmartMotor masterMotor) {
        follow(masterMotor.getDeviceID());
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
    public double getOutputCurrent() {
        return _talon.getOutputCurrent();
    }

    @Override
    public int getDeviceID() {
        return _talon.getDeviceID();
    }

    @Override
    public int getBaseID() {
        return _talon.getBaseID();
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        _talon.setNeutralMode(mode);
    }

    @Override
    public void setSensorPhase(boolean phase) {
        _talon.setSensorPhase(phase);
    }

    @Override
    public void setSensor(FeedbackDevice device) {
        _talon.configSelectedFeedbackSensor(device, 0, 0);
    }

    @Override
    public void setAllowableClosedLoopError(int error) {
        _talon.configAllowableClosedloopError(0, error, 0);
    }

    @Override
    public void setClosedLoopRamp(double secondsFromNeutralToFull) {
        _talon.configClosedloopRamp(secondsFromNeutralToFull, 0);
    }

    @Override
    public void setOpenLoopRamp(double secondsFromNeutralToFull) {
        _talon.configOpenloopRamp(secondsFromNeutralToFull, 0);
    }

    @Override
    public int getSensorPosition() {
        return _talon.getSelectedSensorPosition(0);
    }

    @Override
    public void setSensorPosition(int absolutePosition) {
        _talon.setSelectedSensorPosition(absolutePosition, 0, 0);
    }

    @Override
    public double getClosedLoopTarget() {
        return _talon.getClosedLoopTarget(0);
    }

    @Override
    public boolean getForwardLimitSwitch() {
        return _talon.getSensorCollection().isFwdLimitSwitchClosed();
    }

    @Override
    public boolean getReverseLimitSwitch() {
        return _talon.getSensorCollection().isRevLimitSwitchClosed();
    }

    @Override
    public int getClosedLoopError() {
        return _talon.getClosedLoopError(0);
    }

    @Override
    public int getPulseWidthPosition() {
        return _talon.getSensorCollection().getPulseWidthPosition();
    }

    @Override
    public void set_kF(int slot, double kF) {
        _talon.config_kF(0, kF, 0);
    }

    @Override
    public void setPeakOutputForward(double percentOut) {
        _talon.configPeakOutputForward(percentOut, 0);
    }

    @Override
    public void setPeakOutputReverse(double percentOut) {

    }

    @Override
    public void setNominalOutputForward(double percentOut) {

    }

    @Override
    public void setNominalOutputReverse(double percentOut) {

    }
}
