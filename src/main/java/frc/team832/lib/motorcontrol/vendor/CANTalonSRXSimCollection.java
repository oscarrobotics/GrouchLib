package frc.team832.lib.motorcontrol.vendor;
import com.ctre.phoenix.motorcontrol.TalonSRXSimCollection;

import frc.team832.lib.motorcontrol.SmartMCSimCollection;

public class CANTalonSRXSimCollection implements SmartMCSimCollection {
    private final TalonSRXSimCollection m_simCollection;

    public CANTalonSRXSimCollection(CANTalonSRX controller) {
        m_simCollection = controller.getBaseController().getSimCollection();
    }

    public void setBusVoltage(double voltage) {
        m_simCollection.setBusVoltage(voltage);
    }

    public void setSensorPosition(double position) {
        m_simCollection.setQuadratureRawPosition((int)position);
    }

    /**
     * Assumes native ticks
     * @param velocity Velocity in native Falcon units (2048/100ms)
     */
    public void setSensorVelocity(double velocity) {
        m_simCollection.setQuadratureVelocity((int)velocity);
    }
}
