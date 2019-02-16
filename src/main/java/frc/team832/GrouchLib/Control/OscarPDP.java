package frc.team832.GrouchLib.Control;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import frc.team832.GrouchLib.OscarCANDevice;

public class OscarPDP {

    private PowerDistributionPanel _pdp;

    private boolean onBus;

    public OscarPDP(int canID) {
        _pdp = new PowerDistributionPanel(canID);

        onBus = _pdp.getVoltage() > 0; // TODO: better way to do this?
        OscarCANDevice.addDevice(new OscarCANDevice(canID, onBus, "PDP"));
    }

    public double getTotalCurrent() {
        return onBus ? _pdp.getTotalCurrent() : -1;
    }

    public double getTotalPower() {
        return onBus ? _pdp.getTotalPower() : -1;
    }

    public double getChannelCurrent(int channel) {
        return onBus ? _pdp.getCurrent(channel) : -1;
    }

    public double getChannelPower(int channel) { return onBus ? getChannelCurrent(channel) * getVoltage() : -1; }

    public double getTemperature() { return onBus ? _pdp.getTemperature() : -1; }

    public double getVoltage() { return onBus ?_pdp.getVoltage() : -1; }

    public PowerDistributionPanel getInstance() {
        return _pdp;
    }
}
