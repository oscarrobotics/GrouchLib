package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;
import frc.team832.GrouchLib.Motors.ISmartMotor;

public abstract class SmartMechanism {

	private ISmartMotor _smartMotor;
	private OscarMechanismPositionList _presetPositions;

	public SmartMechanism(ISmartMotor smartMotor, OscarMechanismPositionList presetPositions) {
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

	public OscarMechanismPosition getPresetPosition(String index) {
		OscarMechanismPosition presetPos = _presetPositions.getByIndex(index);
		return _presetPositions.getByIndex(index);
	}

	public void setPosition(OscarMechanismPosition position) {
		_smartMotor.setPosition(position.getTarget());
	}

	public void setPosition(String index) {
		System.out.println("INDEX REQ: " + index);
		setPosition(getPresetPosition(index));
	}

	public boolean getAtTarget(){
		return _smartMotor.getClosedLoopError() <= 20;
	}

	public void setPID(double kP, double kI, double kD){
		_smartMotor.setkP(kP);
		_smartMotor.setkI(kI);
		_smartMotor.setkD(kD);
	}

	public void setUpperLimit(int limit){
		_smartMotor.setUpperLimit(limit);
	}

	public void setLowerLimit(int limit){
		_smartMotor.setLowerLimit(limit);
	}

	public void resetSensor(){
		_smartMotor.resetSensor();
	}

	public void stop() {
		_smartMotor.stopMotor();
	}

	public ISmartMotor getMotor(){
		return  _smartMotor;
	}

}
