package frc.team832.GrouchLib.Motors;

public interface GeniusMotor extends CANSmartMotor {

    void fillProfileBuffer(double[][] profile, int size);

    void setMotionProfile(int value);
}
