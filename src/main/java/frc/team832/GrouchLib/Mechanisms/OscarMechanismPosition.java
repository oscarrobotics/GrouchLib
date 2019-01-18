package frc.team832.GrouchLib.Mechanisms;

public class OscarMechanismPosition {
	private String _index;
	private int _target;

	public OscarMechanismPosition(String index, int target) {
		_index = index;
		_target = target;
	}

	public String getIndex() {
		return _index;
	}

	public int getTarget() {
		return _target;
	}
}
