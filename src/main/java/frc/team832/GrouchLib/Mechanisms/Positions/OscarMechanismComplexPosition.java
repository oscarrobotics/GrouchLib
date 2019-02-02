package frc.team832.GrouchLib.Mechanisms.Positions;

import frc.team832.GrouchLib.Util.Tuple;

public class OscarMechanismComplexPosition {

	private String _index;
	private Tuple<OscarMechanismPosition, OscarMechanismPosition> _complexPosition;

	public OscarMechanismComplexPosition(String index, OscarMechanismPosition pos1, OscarMechanismPosition pos2) {
		_index = index;
		_complexPosition = new Tuple<>(pos1, pos2);
	}

	public OscarMechanismComplexPosition(String index, OscarMechanismPositionList pos1List, OscarMechanismPositionList pos2List) {
		_index = index;
		_complexPosition = new Tuple<>(pos1List.getByIndex(index), pos2List.getByIndex(index));
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
