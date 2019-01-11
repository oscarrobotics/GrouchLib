package frc.team832.GrouchLib.Motion;

import frc.team832.GrouchLib.Motors.IOscarSimpleMotor;

public class OscarSimpleMechanism {

    private IOscarSimpleMotor m_simpleMotor;

    public OscarSimpleMechanism(IOscarSimpleMotor simpleMotor) {
        m_simpleMotor = simpleMotor;
    }

    public void setPower(double power) {
        m_simpleMotor.set(power);
    }

}
