package frc.team832.GrouchLib.motorcontrol;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.MotionProfileStatus;

public interface GeniusMC extends SmartCANMC {

    void fillProfileBuffer(double[][] profile, int size);

    void setMotionProfile(int value);

    void bufferAndSendMP();

    ErrorCode getMotionProfileStatus(MotionProfileStatus status);

    boolean isMPFinished();

    void setIZone(int iZone);

	void setMotionMagcArbFF (double position, double arbFF);

	void configMotionMagic(int vel, int accel);
}
