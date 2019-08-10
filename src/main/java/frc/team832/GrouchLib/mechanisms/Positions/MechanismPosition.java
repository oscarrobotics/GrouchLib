package frc.team832.GrouchLib.mechanisms.Positions;

public class MechanismPosition {

	private String _index;
	private double _target;

	public MechanismPosition(String index, double target) {
		_index = index;
		_target = target;
	}

	public String getIndex() {
		return _index;
	}

	public double getTarget() {
		return _target;
	}
}
