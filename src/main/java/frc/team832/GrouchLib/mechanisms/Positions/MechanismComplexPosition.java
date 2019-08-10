package frc.team832.GrouchLib.mechanisms.Positions;

import frc.team832.GrouchLib.util.Tuple;

public class MechanismComplexPosition {

	private String _index;
	private Tuple<MechanismPosition, MechanismPosition> _complexPosition;

	public MechanismComplexPosition(String index, MechanismPosition pos1, MechanismPosition pos2) {
		_index = index;
		_complexPosition = new Tuple<>(pos1, pos2);
	}

	public MechanismComplexPosition(String index, MechanismPositionList pos1List, MechanismPositionList pos2List) {
		_index = index;
		_complexPosition = new Tuple<>(pos1List.getByIndex(index), pos2List.getByIndex(index));
	}

	public String getIndex() {
		return _index;
	}

	public Tuple<MechanismPosition, MechanismPosition> getPositionPair() {
		return _complexPosition;
	}

	public MechanismPosition getPrimaryPosition() { return _complexPosition.primary; }

	public MechanismPosition getSecondaryPosition() {return _complexPosition.secondary; }
}
