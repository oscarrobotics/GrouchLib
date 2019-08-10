package frc.team832.GrouchLib.motorcontrol;

public class SmartCANMCGroup implements SmartCANMC {

    private final SmartCANMC m_masterMotor;
    private final CANMC[] m_slaveMotors;
    private boolean m_isInverted = false;

    public SmartCANMCGroup(SmartCANMC masterMotor, CANMC... slaveMotors) {
        m_masterMotor = masterMotor;
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
        for (CANMC canMotor : m_slaveMotors) {
            canMotor.disable();
        }
    }

    @Override
    public void stopMotor() {
        m_masterMotor.stopMotor();
    }

    @Override
    public void follow(int canID) {
        for (CANMC slaveMotor : m_slaveMotors) {
            slaveMotor.follow(canID);
        }
    }

    @Override
    public void follow(CANMC masterMotor) {
        for (CANMC slaveMotor : m_slaveMotors) {
            slaveMotor.follow(masterMotor);
        }
    }

    @Override
    public double getInputVoltage() {
        double curTotal = 0.0;
        curTotal += m_masterMotor.getInputVoltage();
        for (CANMC slaveMotor : m_slaveMotors) {
            curTotal += slaveMotor.getInputVoltage();
        }
        return curTotal / (m_slaveMotors.length + 1);
    }

    @Override
    public double getOutputVoltage() {
        double curTotal = 0.0;
        curTotal += m_masterMotor.getOutputVoltage();
        for (CANMC slaveMotor : m_slaveMotors) {
            curTotal += slaveMotor.getOutputVoltage();
        }
        return curTotal / (m_slaveMotors.length + 1);
    }

    @Override
    public double getOutputCurrent() {
        double curTotal = 0.0;
        curTotal += m_masterMotor.getOutputCurrent();
        for (CANMC slaveMotor : m_slaveMotors) {
            curTotal += slaveMotor.getOutputCurrent();
        }
        return curTotal;
    }

    @Override
    public void resetSettings() {
        m_masterMotor.resetSettings();
        for (CANMC motor : m_slaveMotors) {
            motor.resetSettings();
        }
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
        for (CANMC slaveMotor : m_slaveMotors) {
            slaveMotor.setNeutralMode(mode);
        }
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
    public double getSensorVelocity() {
        return m_masterMotor.getSensorVelocity();
    }

    @Override
    public double getSensorPosition() {
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
    public void setForwardSoftLimit(int limit) {
        m_masterMotor.setForwardSoftLimit(limit);
    }

    @Override
    public void setReverseSoftLimit(int limit) {
        m_masterMotor.setReverseSoftLimit(limit);
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

    @Override
    public void setArbFFPos(double arbFF, double pos) {
        //not yet implemented
    }

    @Override
    public void setPeakCurrentLimit(int peak) {

    }

    @Override
    public boolean atTarget() {
        return Math.abs(getTargetPosition() - getSensorPosition()) < 20;
    }

    @Override
    public void setMotionMagic(double pos){}
}
