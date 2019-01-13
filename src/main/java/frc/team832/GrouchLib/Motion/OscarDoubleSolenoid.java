package frc.team832.GrouchLib.Motion;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.team832.GrouchLib.Control.OscarPCM;

public class OscarDoubleSolenoid {

    private DoubleSolenoid _solenoid;

    public OscarDoubleSolenoid(OscarPCM pcm, int pcmChannelF, int pcmChannelR) {
        _solenoid = new DoubleSolenoid(pcm.getDeviceID(), pcmChannelF, pcmChannelR);
    }

    public void forward() {
        _solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void reverse() {
        _solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void off() {
        _solenoid.set(DoubleSolenoid.Value.kOff);
    }

    public DoubleSolenoid.Value get() { return _solenoid.get();}

}
