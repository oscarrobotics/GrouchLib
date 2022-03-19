package frc.team832.lib.motorcontrol.vendor;
import com.ctre.phoenix.motorcontrol.TalonFXSimCollection;

import frc.team832.lib.motorcontrol.SmartMCSimCollection;

public class CANTalonFXSimCollection implements SmartMCSimCollection {
    private final CANTalonFX controller;
    private TalonFXSimCollection m_simCollection;
    
    public CANTalonFXSimCollection(CANTalonFX controller) {
        this.controller = controller;
        m_simCollection = controller.getBaseController().getSimCollection();
    }

    public void setBusVoltage(double voltage) {
        m_simCollection.setBusVoltage(voltage);
    }

    public void setSensorPosition(double position) {
        m_simCollection.setIntegratedSensorRawPosition((int)position * 2048);
    }

    /**
     * Assumes native ticks
     * @param velocity Velocity in native Falcon units (2048/100ms)
     */
    public void setSensorVelocity(double velocity) {
        if (controller.getInverted()) velocity *= -1;
        double rp100ms = velocity / 600;
        int ticks = (int) (rp100ms * 2048);
        m_simCollection.setIntegratedSensorVelocity((int)ticks);
    }
}
