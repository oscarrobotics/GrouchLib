package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Mechanisms.Positions.MechanismMotionProfile;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.GrouchLib.Motors.GeniusMotor;
import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.MotionProfileStatus;

public class GeniusMechanism {

    private GeniusMotor _geniusMotor;
    private MechanismPositionList _presetPositions;

    public GeniusMechanism(GeniusMotor geniusMotor, MechanismPositionList presetPositions) {
        _geniusMotor = geniusMotor;
        _presetPositions = presetPositions;
    }

    public double getTargetPosition() { return _geniusMotor.getTargetPosition(); }

    public double getCurrentPosition() {
        return _geniusMotor.getSensorPosition();
    }

    public double getVelocity() {
        return _geniusMotor.getSensorVelocity();
    }

    public MechanismPosition getPresetPosition(String index) {
        MechanismPosition presetPos = _presetPositions.getByIndex(index);
        return _presetPositions.getByIndex(index);
    }

    public void setPosition(MechanismPosition position) {
        _geniusMotor.setPosition(position.getTarget());
    }

    public void setPosition(String index) {
        System.out.println("INDEX REQ: " + index);
        setPosition(getPresetPosition(index));
    }

    public boolean getAtTarget(){
        return _geniusMotor.getClosedLoopError() <= 20;
    }

    public void setPIDF(double kP, double kI, double kD, double kF){
        _geniusMotor.setkP(kP);
        _geniusMotor.setkI(kI);
        _geniusMotor.setkD(kD);
        _geniusMotor.setkF(kF);
    }

    public void setLimits(int lowerLimit, int upperLimit) {
        _geniusMotor.setReverseSoftLimit(lowerLimit);
        _geniusMotor.setForwardSoftLimit(upperLimit);
    }

    public void setUpperLimit(int limit){
        _geniusMotor.setForwardSoftLimit(limit);
    }

    public void setLowerLimit(int limit){
        _geniusMotor.setReverseSoftLimit(limit);
    }

    public void resetSensor(){
        _geniusMotor.resetSensor();
    }

    public void stop() {
        _geniusMotor.stopMotor();
    }

    public GeniusMotor getMotor(){
        return  _geniusMotor;
    }

    public void bufferTrajectory(MechanismMotionProfile profile){
        _geniusMotor.fillProfileBuffer(profile.talonTrajectory(), profile.length());
    }

    public void setMotionProfile(int value){
        _geniusMotor.setMotionProfile(value);
    }

    public void bufferAndSendMP() {
        _geniusMotor.bufferAndSendMP();
    }

    public ErrorCode getMotionProfileStatus(MotionProfileStatus status) {
        return _geniusMotor.getMotionProfileStatus(status);
    }

    public boolean isMPFinished() {
        return _geniusMotor.isMPFinished();
    }

    public void setIZone(int iZone) {
        _geniusMotor.setIZone(iZone);
    }
}
