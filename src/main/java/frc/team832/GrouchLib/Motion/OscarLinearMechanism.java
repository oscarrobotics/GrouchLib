package frc.team832.GrouchLib.Motion;

import frc.team832.GrouchLib.Motors.IOscarSmartMotor;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class OscarLinearMechanism {

    private IOscarSmartMotor m_linearMotor;

    public OscarLinearMechanism(IOscarSmartMotor linearMotor) {
        m_linearMotor = linearMotor;
    }

    public double getPosition() {
        return m_linearMotor.getPosition();
    }

    public void setPosition(double position) {
        m_linearMotor.setMode(ControlMode.Position);
        m_linearMotor.set(position);
    }

    public void stop() {
        m_linearMotor.stopMotor();
    }
}
