package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.MechanismPositionList;
import frc.team832.GrouchLib.Motors.SmartMotor;

public abstract class SmartMechanism {

	private SmartMotor _smartMotor;
	private MechanismPositionList _presetPositions;

	public SmartMechanism(SmartMotor smartMotor, MechanismPositionList presetPositions) {
		_smartMotor = smartMotor;
		_presetPositions = presetPositions;
	}

	public double getTargetPosition() { return _smartMotor.getTargetPosition(); }

	public double getCurrentPosition() {
		return _smartMotor.getSensorPosition();
	}

	public double getVelocity() {
		return _smartMotor.getSensorVelocity();
	}

	public MechanismPosition getPresetPosition(String index) {
		MechanismPosition presetPos = _presetPositions.getByIndex(index);
		return _presetPositions.getByIndex(index);
	}

	public void setPosition(MechanismPosition position) {
		_smartMotor.setPosition(position.getTarget());
	}

	public void setPosition(double pos){
		_smartMotor.setPosition(pos);
	}

	public void setPosition(String index) {
		System.out.println("INDEX REQ: " + index);
		setPosition(getPresetPosition(index));
	}

	public boolean getAtTarget(){
		return _smartMotor.getClosedLoopError() <= 20;
	}

	public void setPIDF(double kP, double kI, double kD, double kF){
		_smartMotor.setkP(kP);
		_smartMotor.setkI(kI);
		_smartMotor.setkD(kD);
		_smartMotor.setkF(kF);
	}

	public void setUpperLimit(int limit){
		_smartMotor.setForwardSoftLimit(limit);
	}

	public void setLowerLimit(int limit){
		_smartMotor.setReverseSoftLimit(limit);
	}

	public void resetSensor(){
		_smartMotor.resetSensor();
	}

	public void stop() {
		_smartMotor.stopMotor();
	}

	public SmartMotor getMotor(){
		return  _smartMotor;
	}

	public int getClosedLoopError(){
		return _smartMotor.getClosedLoopError();
	}
}
