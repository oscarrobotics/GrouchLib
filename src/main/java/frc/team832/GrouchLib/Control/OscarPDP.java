package frc.team832.GrouchLib.Control;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class OscarPDP {

    private PowerDistributionPanel _pdp;

    public OscarPDP(int canID) {
        _pdp = new PowerDistributionPanel(canID);
    }

    public double getTotalCurrent() {
        return _pdp.getTotalCurrent();
    }

    public double getTotalPower() {
        return _pdp.getTotalPower();
    }

    public double getChannelCurrent(int channel) {
        return _pdp.getCurrent(channel);
    }

    public double getTemperature() {
        return _pdp.getTemperature();
    }

    public double getVoltage() {
        return _pdp.getVoltage();
    }
}
