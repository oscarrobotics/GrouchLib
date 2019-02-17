package frc.team832.GrouchLib.Motors;

public interface IOscarGeniusMotor extends IOscarCANSmartMotor {
    void startFilling(double[][] profile, int size);

    void setMotionProfile(int value);
}
