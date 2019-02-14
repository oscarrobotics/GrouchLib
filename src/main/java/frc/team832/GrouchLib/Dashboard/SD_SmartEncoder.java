package frc.team832.GrouchLib.Dashboard;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team832.GrouchLib.Motors.IOscarSmartMotor;

public class SD_SmartEncoder extends SendableBase {

    private IOscarSmartMotor _sourceMotor;

    public SD_SmartEncoder(IOscarSmartMotor sourceMotor) {
        _sourceMotor = sourceMotor;
    }

    private double getPosition() {
        return _sourceMotor.getSensorPosition();
    }

    private double getVelocity() {
        return _sourceMotor.getSensorVelocity();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Encoder");
        builder.addDoubleProperty("Distance", this::getPosition, null);
        builder.addDoubleProperty("Speed", this::getVelocity, null);
    }
}
