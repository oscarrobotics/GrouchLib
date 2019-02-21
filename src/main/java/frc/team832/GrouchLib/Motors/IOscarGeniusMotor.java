package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.MotionProfileStatus;

public interface IOscarGeniusMotor extends IOscarCANSmartMotor {

    void fillProfileBuffer(double[][] profile, int size);

    void setMotionProfile(int value);

    void bufferAndSendMP();

    ErrorCode getMotionProfileStatus(MotionProfileStatus status);

    boolean isMPFinished();
}
