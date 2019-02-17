package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class OscarCANSmartMotorGroup implements IOscarCANSmartMotor {

    private final IOscarCANSmartMotor m_masterMotor;
    private final IOscarCANMotor[] m_slaveMotors;
    private boolean m_isInverted = false;

    public OscarCANSmartMotorGroup(IOscarCANSmartMotor masterMotor, IOscarCANMotor... slaveMotors) {
        m_masterMotor = masterMotor;
        m_slaveMotors = slaveMotors.clone();
//        follow(masterMotor);
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
    public int getCurrentPosition() {
        return m_masterMotor.getCurrentPosition();
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
        for (IOscarCANMotor canMotor : m_slaveMotors) {
            canMotor.disable();
        }
    }

    @Override
    public void stopMotor() {
        m_masterMotor.stopMotor();
    }

    @Override
    public void follow(int canID) {
        for (IOscarCANMotor slaveMotor : m_slaveMotors) {
            slaveMotor.follow(canID);
        }
    }

    @Override
    public void follow(IOscarCANMotor masterMotor) {
        for (IOscarCANMotor slaveMotor : m_slaveMotors) {
            slaveMotor.follow(masterMotor);
        }
    }

    @Override
    public double getInputVoltage() {
        double curTotal = 0.0;
        curTotal += m_masterMotor.getInputVoltage();
        for (IOscarCANMotor slaveMotor : m_slaveMotors) {
            curTotal += slaveMotor.getInputVoltage();
        }
        return curTotal / (m_slaveMotors.length + 1);
    }

    @Override
    public double getOutputVoltage() {
        double curTotal = 0.0;
        curTotal += m_masterMotor.getOutputVoltage();
        for (IOscarCANMotor slaveMotor : m_slaveMotors) {
            curTotal += slaveMotor.getOutputVoltage();
        }
        return curTotal / (m_slaveMotors.length + 1);
    }

    @Override
    public double getOutputCurrent() {
        double curTotal = 0.0;
        curTotal += m_masterMotor.getOutputCurrent();
        for (IOscarCANMotor slaveMotor : m_slaveMotors) {
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
        for (IOscarCANMotor slaveMotor : m_slaveMotors) {
            slaveMotor.setNeutralMode(mode);
        }
    }

    @Override
    public void setSensorPhase(boolean phase) {
        m_masterMotor.setSensorPhase(phase);
    }

    @Override
    public void setSensorType(FeedbackDevice device) {
        m_masterMotor.setSensorType(device);
    }

    @Override
    public void setAllowableClosedLoopError(int error) {
        m_masterMotor.setAllowableClosedLoopError(error);
    }

    @Override
    public int getAllowableClosedLoopError() {
        return m_masterMotor.getAllowableClosedLoopError();
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
    public int getSensorVelocity() {
        return m_masterMotor.getSensorVelocity();
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
    public double getTargetPosition() {
        return m_masterMotor.getTargetPosition();
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

    @Override
    public void setkP(double kP) {
        m_masterMotor.setkP(kP);
    }

    @Override
    public void setkI(double kI) {
        m_masterMotor.setkI(kI);
    }

    @Override
    public void setkD(double kD) {
        m_masterMotor.setkD(kD);
    }

    @Override
    public void setkF(double kF) {
        m_masterMotor.setkF(kF);
    }

    @Override
    public void setkP(double kP, int slotID) {
        m_masterMotor.setkP(kP, slotID);
    }

    @Override
    public void setkI(double kI, int slotID) {
        m_masterMotor.setkI(kI, slotID);
    }

    @Override
    public void setkD(double kD, int slotID) {
        m_masterMotor.setkD(kD, slotID);
    }

    @Override
    public void setkF(double kF, int slotID) {
        m_masterMotor.setkF(kF, slotID);
    }

    @Override
    public void setUpperLimit(int limit) {
        m_masterMotor.setUpperLimit(limit);
    }

    @Override
    public void setLowerLimit(int limit) {
        m_masterMotor.setLowerLimit(limit);
    }

    @Override
    public void resetSensor() {
        m_masterMotor.resetSensor();
    }

    @Override
    public void setVelocity(double rpmVal) {
        m_masterMotor.setVelocity(rpmVal);
    }

    @Override
    public void setPosition(double posVal) {
        m_masterMotor.setPosition(posVal);
    }
}
