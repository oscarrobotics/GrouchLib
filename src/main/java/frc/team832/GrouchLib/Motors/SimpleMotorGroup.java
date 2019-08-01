package frc.team832.GrouchLib.Motors;

public class SimpleMotorGroup implements SimpleMotor {

    private final SimpleMotor[] m_simpleMotors;
    private boolean m_isInverted = false;

    public SimpleMotorGroup(SimpleMotor simpleMotor, SimpleMotor... simpleMotors) {
        m_simpleMotors = new SimpleMotor[simpleMotors.length + 1];
        m_simpleMotors[0] = simpleMotor;
        for (int i = 0; i < simpleMotors.length; i++) {
            m_simpleMotors[i + 1] = simpleMotors[i];
        }
    }

    @Override
    public void set(double speed) {
        for (SimpleMotor simpleMotor : m_simpleMotors) {
            simpleMotor.set(m_isInverted ? -speed : speed);
        }
    }

    @Override
    public double get() {
        if (m_simpleMotors.length > 0) {
            return m_simpleMotors[0].get() * (m_isInverted ? -1 : 1);
        }
        return 0.0;
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
    public void setNeutralMode(NeutralMode mode) {
        for (SimpleMotor simpleMotor : m_simpleMotors) {
            simpleMotor.setNeutralMode(mode);
        }
    }

    @Override
    public void disable() {
        for (SimpleMotor simpleMotor : m_simpleMotors) {
            simpleMotor.disable();
        }
    }

    @Override
    public void stopMotor() {
        for (SimpleMotor simpleMotor : m_simpleMotors) {
            simpleMotor.stopMotor();
        }
    }
}
