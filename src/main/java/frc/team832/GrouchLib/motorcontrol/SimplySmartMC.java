package frc.team832.GrouchLib.motorcontrol;

import frc.team832.GrouchLib.sensors.RemoteEncoder;
import frc.team832.GrouchLib.util.MiniPID;

public class SimplySmartMC implements PIDMC {

	SimpleMC _motor;
	RemoteEncoder _encoder;
	MiniPID PID;
	double targetPos;

	public SimplySmartMC(SimpleMC motor, RemoteEncoder encoder) {
		_motor = motor;
		_encoder = encoder;
		PID = new MiniPID(0,0,0);
	}

	@Override
	public double getSensorPosition() {
		return _encoder.getSensorPosition();
	}

	@Override
	public double getSensorVelocity() {
		return _encoder.getSensorVelocity();
	}

	@Override
	public void setSensorPosition(int absolutePosition) {
		// not yet implemented...
	}

	@Override
	public void setPosition(double pos){
		targetPos = pos;
		double output = PID.getOutput(_encoder.getSensorPosition(), pos);
		_motor.set(output);
	}

	@Override
	public boolean atTarget(){
		return Math.abs(targetPos - _encoder.getSensorPosition()) < 20;
	}

	@Override
	public void resetSensor() {
		setSensorPosition(0);
	}

	@Override
	public void setkP(double kP) {
		PID.setP(kP);
	}

	@Override
	public void setkI(double kI) {
		PID.setI(kI);
	}

	@Override
	public void setkD(double kD) {
		PID.setD(kD);
	}

	@Override
	public void setkF(double kF) {

	}

	@Override
	public void setkP(double kP, int slotID) {

	}

	@Override
	public void setkI(double kI, int slotID) {

	}

	@Override
	public void setkD(double kD, int slotID) {

	}

	@Override
	public void setkF(double kF, int slotID) {

	}

	@Override
	public void setAllowableClosedLoopError(int error) {

	}

	@Override
	public int getAllowableClosedLoopError() {
		return 0;
	}

	@Override
	public int getClosedLoopError() {
		return 0;
	}

	@Override
	public double getTargetPosition() {
		return 0;
	}
}
