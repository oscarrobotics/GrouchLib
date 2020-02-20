package frc.team832.lib.motorcontrol.base;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.MotionProfileStatus;

/** @deprecated Use the new {@link frc.team832.lib.motorcontrol2 } package
 */
@Deprecated
public interface GeniusMC<B> extends SmartCANMC<B> {

    void fillProfileBuffer(double[][] profile, int size);

    void setMotionProfile(int value);

    void bufferAndSendMP();

    ErrorCode getMotionProfileStatus(MotionProfileStatus status);

    boolean isMPFinished();

    void setIZone(int iZone);

	void setMotionMagicArbFF(double position, double arbFF);

	void configMotionMagic(int vel, int accel);
}
