package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismMotionProfile;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;
import frc.team832.GrouchLib.Motors.IOscarGeniusMotor;
import jaci.pathfinder.Trajectory;

public class OscarGeniusMechanism {

    private IOscarGeniusMotor _geniusMotor;
    private OscarMechanismPositionList _presetPositions;

    public OscarGeniusMechanism(IOscarGeniusMotor geniusMotor, OscarMechanismPositionList presetPositions) {
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

    public OscarMechanismPosition getPresetPosition(String index) {
        OscarMechanismPosition presetPos = _presetPositions.getByIndex(index);
        return _presetPositions.getByIndex(index);
    }

    public void setPosition(OscarMechanismPosition position) {
        _geniusMotor.setPosition(position.getTarget());
    }

    public void setPosition(String index) {
        System.out.println("INDEX REQ: " + index);
        setPosition(getPresetPosition(index));
    }

    public boolean getAtTarget(){
        return _geniusMotor.getClosedLoopError() <= 20;
    }

    public void setPID(double kP, double kI, double kD){
        _geniusMotor.setkP(kP);
        _geniusMotor.setkI(kI);
        _geniusMotor.setkD(kD);
    }

    public void setUpperLimit(int limit){
        _geniusMotor.setUpperLimit(limit);
    }

    public void setLowerLimit(int limit){
        _geniusMotor.setLowerLimit(limit);
    }

    public void resetSensor(){
        _geniusMotor.resetSensor();
    }

    public void stop() {
        _geniusMotor.stopMotor();
    }

    public IOscarGeniusMotor getMotor(){
        return  _geniusMotor;
    }

    public void startFillingTrajectory(Trajectory traj){
        _geniusMotor.startFilling(OscarMechanismMotionProfile.pathfinderFormatToTalon(traj), traj.length());
    }

    public void setMotionProfile(int value){
        _geniusMotor.setMotionProfile(value);
    }
}
