package frc.team832.lib.motorcontrol.vendor;
import com.ctre.phoenix.motorcontrol.TalonFXSimCollection;

import frc.team832.lib.motorcontrol.SmartMCSimCollection;

public class CANTalonFXSimCollection implements SmartMCSimCollection {
    private TalonFXSimCollection m_simCollection;
    
    public CANTalonFXSimCollection(CANTalonFX controller) {
        m_simCollection = controller.getBaseController().getSimCollection();
    }

    public void setBusVoltage(double voltage) {
        m_simCollection.setBusVoltage(voltage);
    }

    public void setSensorPosition(double position) {
        m_simCollection.setIntegratedSensorRawPosition((int)position);
    }

    /**
     * Assumes native ticks
     * @param velocity Velocity in native Falcon units (2048/100ms)
     */
    public void setSensorVelocity(double velocity) {
        m_simCollection.setIntegratedSensorVelocity((int)velocity);
    }
}
