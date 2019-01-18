package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Util.Tuple;

public class OscarMechanismComplexPosition {

	private String _index;
	private Tuple<OscarMechanismPosition, OscarMechanismPosition> _complexPosition;

	public OscarMechanismComplexPosition(String index, OscarMechanismPosition pos1, OscarMechanismPosition pos2) {
		_index = index;
		_complexPosition = new Tuple<>(pos1, pos2);
	}

	public String getIndex() {
		return _index;
	}

	public Tuple<OscarMechanismPosition, OscarMechanismPosition> getPositionPair() {
		return _complexPosition;
	}

	public OscarMechanismPosition getPrimaryPosition() { return _complexPosition.primary; }

	public OscarMechanismPosition getSecondaryPosition() {return _complexPosition.secondary; }
}
