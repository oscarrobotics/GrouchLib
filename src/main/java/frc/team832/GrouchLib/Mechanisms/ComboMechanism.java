package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;

public class ComboMechanism {

    private SmartMechanism _primaryMech, _secondaryMech;

    public ComboMechanism(SmartMechanism primaryMech, SmartMechanism secondaryMech) {
        _primaryMech = primaryMech;
        _secondaryMech = secondaryMech;
    }

    public double getPrimaryTargetPosition() { return _primaryMech.getTargetPosition(); }

    public double getSecondaryTargetPosition() { return _secondaryMech.getTargetPosition(); }

    public double getPrimaryCurrentPosition() {
        return _primaryMech.getCurrentPosition();
    }

    public double getSecondaryCurrentPosition() {
        return _secondaryMech.getCurrentPosition();
    }

    public double getPrimaryVelocity() {
        return _primaryMech.getVelocity();
    }

    public double getSensorVelocity() {
        return _secondaryMech.getVelocity();
    }

    public OscarMechanismPosition getPresetPosition(String index) {
        return _primaryMech.getPresetPosition(index);
    }

    public void setPosition(OscarMechanismPosition position) {
        _secondaryMech.setPosition(position);
        _primaryMech.setPosition(position);
    }

    public void setPosition(String index) {
        System.out.println("INDEX REQ: " + index);
        setPosition(getPresetPosition(index));
    }

    public boolean getAtTarget(){
        return _secondaryMech.getAtTarget() && _primaryMech.getAtTarget();
    }

    public void setPID(double kP, double kI, double kD){
        _primaryMech.setPID(kP, kI, kD);
        _secondaryMech.setPID(kP, kI, kD);
    }

    public void setPrimaryUpperLimit(int limit){
        _primaryMech.setUpperLimit(limit);
    }

    public void setSecondaryUpperLimit(int limit){
        _secondaryMech.setUpperLimit(limit);
    }

    public void setPrimaryLowerLimit(int limit){
        _primaryMech.setLowerLimit(limit);
    }

    public void setSecondaryLowerLimit(int limit){
        _secondaryMech.setLowerLimit(limit);
    }

    public void resetSensor(){
        _primaryMech.resetSensor();
        _secondaryMech.resetSensor();
    }

    public void stop() {
        _secondaryMech.stop();
        _primaryMech.stop();
    }
}
