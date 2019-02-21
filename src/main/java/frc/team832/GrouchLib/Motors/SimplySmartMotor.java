package frc.team832.GrouchLib.Motors;

import frc.team832.GrouchLib.Sensors.RemoteEncoder;

public class SimplySmartMotor implements PIDMotor{

	SimpleMotor _motor;
	RemoteEncoder _encoder;

	public SimplySmartMotor(SimpleMotor motor, RemoteEncoder encoder) {
		_motor = motor;
		_encoder = encoder;
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
	public void resetSensor() {
		setSensorPosition(0);
	}

	@Override
	public void setkP(double kP) {

	}

	@Override
	public void setkI(double kI) {

	}

	@Override
	public void setkD(double kD) {

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
