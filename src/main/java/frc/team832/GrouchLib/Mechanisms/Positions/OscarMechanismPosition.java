package frc.team832.GrouchLib.Mechanisms.Positions;

public class OscarMechanismPosition {

	private String _index;
	private double _target;

	public OscarMechanismPosition(String index, double target) {
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
