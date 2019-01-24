package frc.team832.GrouchLib.Control;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class OscarPCM {

    private Compressor _pcm;
    private int _id;

    public OscarPCM(int canID) {
        _id = canID;
        _pcm = new Compressor(canID);
    }

    public void setEnabled(boolean value) {
        _pcm.setClosedLoopControl(value);
    }

    public boolean isEnabled() {
        return _pcm.enabled();
    }

    public double getCurrent() {
        return _pcm.getCompressorCurrent();
    }

    public boolean getPressureSwitch() {
        return _pcm.getPressureSwitchValue();
    }

    public int getDeviceID() {
        return _id;
    }

    public void setOutput(int channel, boolean value) {
        Solenoid tempSolenoid = new Solenoid(_id, channel);
        tempSolenoid.set(value);
    }
}
