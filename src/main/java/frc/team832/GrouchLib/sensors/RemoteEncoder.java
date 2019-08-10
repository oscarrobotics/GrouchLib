package frc.team832.GrouchLib.sensors;

import java.util.function.Supplier;

public class RemoteEncoder {

	private Supplier<Double> _getEncPosition, _getEncVelocity;

	public RemoteEncoder(Supplier<Double> getEncPosition, Supplier<Double> getEncVelocity) {
		_getEncPosition = getEncPosition;
		_getEncVelocity = getEncVelocity;
	}

	public RemoteEncoder(CANifier canifier) {
		_getEncPosition = () -> (double) canifier.getQuadPosition();
		_getEncVelocity = () -> (double) canifier.getQuadVelocity();
	}

	public double getSensorPosition() {
		return _getEncPosition.get();
	}

	public double getSensorVelocity() {
		return _getEncVelocity.get();
	}
}
