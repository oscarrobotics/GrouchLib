package frc.team832.GrouchLib.Control;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import frc.team832.GrouchLib.OscarCANDevice;

public class OscarPCM {

    private Compressor _pcm;
    private int _id;

    private boolean onBus;

    public OscarPCM(int canID) {
        _id = canID;
        _pcm = new Compressor(canID);

        try {
            setEnabled(false);
            onBus = true;
        } catch (Exception ex) {
            onBus = false;
        }
        OscarCANDevice.addDevice(new OscarCANDevice(canID, onBus, "PCM"));
    }

    public void setEnabled(boolean value) {
        if (onBus) {
            _pcm.setClosedLoopControl(value);
        }
    }

    public boolean isEnabled() {
        return onBus && _pcm.enabled();
    }

    public double getCurrent() {
        return onBus ? _pcm.getCompressorCurrent() : -1;
    }

    public boolean getPressureSwitch() {
        return onBus && _pcm.getPressureSwitchValue();
    }

    public int getDeviceID() {
        return _id;
    }

    public void setOutput(int channel, boolean value) {
        if (onBus) {
            Solenoid tempSolenoid = new Solenoid(_id, channel);
            tempSolenoid.set(value);
        }
    }
}
