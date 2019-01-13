package frc.team832.GrouchLib.Motion;

import edu.wpi.first.wpilibj.Solenoid;
import frc.team832.GrouchLib.Control.OscarPCM;

public class OscarSolenoid {

    private Solenoid _solenoid;

    public OscarSolenoid(OscarPCM pcm, int pcmChannel) {
        _solenoid = new Solenoid(pcm.getDeviceID(), pcmChannel);
    }

    public void open() {
        _solenoid.set(true);
    }

    public void close() {
        _solenoid.set(false);
    }

    public boolean get() { return _solenoid.get(); }
}
