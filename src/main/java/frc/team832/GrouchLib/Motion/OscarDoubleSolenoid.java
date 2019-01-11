package frc.team832.GrouchLib.Motion;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class OscarDoubleSolenoid {

    DoubleSolenoid _solenoid;

    public OscarDoubleSolenoid(int pcmID, int pcmChannelF, int pcmChannelR) {
        _solenoid = new DoubleSolenoid(pcmID, pcmChannelF, pcmChannelR);
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


}
