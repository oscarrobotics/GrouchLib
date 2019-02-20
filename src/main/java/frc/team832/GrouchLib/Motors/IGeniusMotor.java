package frc.team832.GrouchLib.Motors;

public interface IGeniusMotor extends ICANSmartMotor {

    void fillProfileBuffer(double[][] profile, int size);

    void setMotionProfile(int value);
}
