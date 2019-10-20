package frc.team832.lib.motorcontrol;

import frc.team832.lib.motorcontrol.base.PIDMC;
import frc.team832.lib.motorcontrol.base.SimpleMC;
import frc.team832.lib.sensors.RemoteEncoder;
import frc.team832.lib.util.MiniPID;

public class SimplySmartMC implements PIDMC {

	private final SimpleMC _motor;
	private final RemoteEncoder _encoder;
	private final MiniPID PID;
	private
	double _openLoopTarget, _closedLoopTarget;
	double _allowableClosedLoopError;

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
		_closedLoopTarget = pos;
		double output = PID.getOutput(_encoder.getSensorPosition(), pos);
		_motor.set(output);
	}

	@Override
	public boolean atTarget(){
		return Math.abs(_closedLoopTarget - _encoder.getSensorPosition()) < 20;
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
	public void setAllowableClosedLoopError(double error) {
		_allowableClosedLoopError = error;
	}

	@Override
	public double getAllowableClosedLoopError() {
		return _allowableClosedLoopError;
	}

	@Override
	public double getClosedLoopError() {
		return _closedLoopTarget - getSensorVelocity();
	}

	@Override
	public double getClosedLoopTarget() {
		return _closedLoopTarget;
	}
}
