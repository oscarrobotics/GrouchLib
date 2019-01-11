package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class OscarSmartMotorGroup implements IOscarSmartMotor {

    private final IOscarSmartMotor m_masterMotor;
    private final IOscarSmartMotor[] m_slaveMotors;
    private boolean m_isInverted = false;

    public OscarSmartMotorGroup(IOscarSmartMotor masterMotor, IOscarSmartMotor... slaveMotors) {
        m_masterMotor = masterMotor;
//        m_slaveMotors = new IOscarSmartMotor[slaveMotors.length];
        m_slaveMotors = slaveMotors.clone();
        follow(masterMotor);
    }

    @Override
    public void set(double value) {
        m_masterMotor.set(m_isInverted ? -value : value);
    }

    @Override
    public double get() {
        return m_masterMotor.get();
    }

    @Override
    public int getPosition() {
        return m_masterMotor.getPosition();
    }

    @Override
    public boolean getInverted() {
        return m_isInverted;
    }

    @Override
    public void setInverted(boolean isInverted) {
        m_isInverted = isInverted;
    }

    @Override
    public void disable() {
        m_masterMotor.disable();
        for (IOscarSmartMotor smartMotor : m_slaveMotors) {
            smartMotor.disable();
        }
    }

    @Override
    public void stopMotor() {
        m_masterMotor.stopMotor();
    }

    @Override
    public void setMode(ControlMode mode) {
        m_masterMotor.setMode(mode);
    }

    @Override
    public void follow(int canID) {
        for (IOscarSmartMotor slaveMotor : m_slaveMotors) {
            slaveMotor.follow(canID);
        }
    }

    @Override
    public void follow(IOscarSmartMotor masterMotor) {
        for (IOscarSmartMotor slaveMotor : m_slaveMotors) {
            slaveMotor.follow(masterMotor);
        }
    }

    @Override
    public double getInputVoltage() {
        double curTotal = 0.0;
        curTotal += m_masterMotor.getInputVoltage();
        for (IOscarSmartMotor slaveMotor : m_slaveMotors) {
            curTotal += slaveMotor.getInputVoltage();
        }
        return curTotal / (m_slaveMotors.length + 1);
    }

    @Override
    public double getOutputVoltage() {
        double curTotal = 0.0;
        curTotal += m_masterMotor.getOutputVoltage();
        for (IOscarSmartMotor slaveMotor : m_slaveMotors) {
            curTotal += slaveMotor.getOutputVoltage();
        }
        return curTotal / (m_slaveMotors.length + 1);
    }

    @Override
    public double getOutputCurrent() {
        double curTotal = 0.0;
        curTotal += m_masterMotor.getOutputCurrent();
        for (IOscarSmartMotor slaveMotor : m_slaveMotors) {
            curTotal += slaveMotor.getOutputCurrent();
        }
        return curTotal;
    }

    @Override
    public int getDeviceID() {
        return m_masterMotor.getDeviceID();
    }

    @Override
    public int getBaseID() {
        return m_masterMotor.getBaseID();
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        m_masterMotor.setNeutralMode(mode);
    }

    @Override
    public void setSensorPhase(boolean phase) {
        m_masterMotor.setSensorPhase(phase);
    }

    @Override
    public void setSensor(FeedbackDevice device) {
        m_masterMotor.setSensor(device);
    }

    @Override
    public void setAllowableClosedLoopError(int error) {
        m_masterMotor.setAllowableClosedLoopError(error);
    }

    @Override
    public void setClosedLoopRamp(double secondsFromNeutralToFull) {
        m_masterMotor.setClosedLoopRamp(secondsFromNeutralToFull);
    }

    @Override
    public void setOpenLoopRamp(double secondsFromNeutralToFull) {
        m_masterMotor.setOpenLoopRamp(secondsFromNeutralToFull);
    }

    @Override
    public int getSensorPosition() {
        return m_masterMotor.getSensorPosition();
    }

    @Override
    public void setSensorPosition(int absolutePosition) {
        m_masterMotor.setSensorPosition(absolutePosition);
    }

    @Override
    public double getClosedLoopTarget() {
        return m_masterMotor.getClosedLoopTarget();
    }

    @Override
    public boolean getForwardLimitSwitch() {
        return m_masterMotor.getForwardLimitSwitch();
    }

    @Override
    public boolean getReverseLimitSwitch() {
        return m_masterMotor.getReverseLimitSwitch();
    }

    @Override
    public int getClosedLoopError() {
        return m_masterMotor.getClosedLoopError();
    }

    @Override
    public int getPulseWidthPosition() {
        return m_masterMotor.getPulseWidthPosition();
    }

    @Override
    public void set_kF(int slot, double kF) {
        m_masterMotor.set_kF(slot, kF);
    }

    @Override
    public void setPeakOutputForward(double percentOut) {
        m_masterMotor.setPeakOutputForward(percentOut);
    }

    @Override
    public void setPeakOutputReverse(double percentOut) {
        m_masterMotor.setPeakOutputReverse(percentOut);
    }

    @Override
    public void setNominalOutputForward(double percentOut) {
        m_masterMotor.setNominalOutputForward(percentOut);
    }

    @Override
    public void setNominalOutputReverse(double percentOut) {
        m_masterMotor.setNominalOutputReverse(percentOut);
    }
}
