package frc.team832.lib.motorcontrol.vendor;

import frc.team832.lib.motorcontrol.SmartMCSimCollection;

public class CANSparkMaxSimCollection implements SmartMCSimCollection {
    private final com.revrobotics.CANSparkMax baseController;
    public CANSparkMaxSimCollection(CANSparkMax controller) {
        this.baseController = controller.getBaseController();
    }

    public void setBusVoltage(double voltage) {
        // controller.setBusVoltage(voltage);
    }

    public void setSensorPosition(double position) {
        // controller.setQuadratureRawPosition(position);
    }

    public void setSensorVelocity(double velocity) {
        // controller.setQuadratureVelocity(velocity);
    }   
}
