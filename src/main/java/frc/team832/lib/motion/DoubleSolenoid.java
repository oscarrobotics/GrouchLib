package frc.team832.lib.motion;

import frc.team832.lib.control.PCM;

public class DoubleSolenoid {

    private edu.wpi.first.wpilibj.DoubleSolenoid _solenoid;

    public DoubleSolenoid(PCM pcm, int pcmChannelF, int pcmChannelR) {
        _solenoid = new edu.wpi.first.wpilibj.DoubleSolenoid(pcm.getDeviceID(), pcmChannelF, pcmChannelR);
    }

    public void forward() {
        _solenoid.set(edu.wpi.first.wpilibj.DoubleSolenoid.Value.kForward);
    }

    public void reverse() {
        _solenoid.set(edu.wpi.first.wpilibj.DoubleSolenoid.Value.kReverse);
    }

    public void off() {
        _solenoid.set(edu.wpi.first.wpilibj.DoubleSolenoid.Value.kOff);
    }

    public edu.wpi.first.wpilibj.DoubleSolenoid.Value get() { return _solenoid.get();}

}
