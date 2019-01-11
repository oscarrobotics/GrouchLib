package frc.team832.GrouchLib.Motion;

import edu.wpi.first.wpilibj.Solenoid;

public class OscarSolenoid {

    Solenoid _solenoid;

    public OscarSolenoid(int pcmID, int pcmChannel) {
        _solenoid = new Solenoid(pcmID, pcmChannel);
    }

    public void open() {
        _solenoid.set(true);
    }

    public void close() {
        _solenoid.set(false);
    }
}
