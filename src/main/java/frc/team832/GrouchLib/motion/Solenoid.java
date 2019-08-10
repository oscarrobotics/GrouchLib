package frc.team832.GrouchLib.motion;

import frc.team832.GrouchLib.control.PCM;

public class Solenoid {

    private edu.wpi.first.wpilibj.Solenoid _solenoid;

    public Solenoid(PCM pcm, int pcmChannel) {
        _solenoid = new edu.wpi.first.wpilibj.Solenoid(pcm.getDeviceID(), pcmChannel);
    }

    public void open() {
        _solenoid.set(true);
    }

    public void close() {
        _solenoid.set(false);
    }

    public boolean get() { return _solenoid.get(); }
}
