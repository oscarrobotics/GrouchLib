package frc.team832.GrouchLib.mechanisms.Positions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MechanismPositionList {

	List<MechanismPosition> _presetPositions;

	public MechanismPositionList(MechanismPosition[] presetPositions) {
		_presetPositions = Arrays.asList(presetPositions);
	}

	public MechanismPosition getByIndex(String index) {
		MechanismPosition presetPos = _presetPositions.stream()
				.filter(pos -> Objects.equals(index, pos.getIndex()))
				.findFirst()
				.orElse(null);
		if (presetPos == null) {
			throw new IndexOutOfBoundsException("Position index not defined: " + index);
		}
		return presetPos;
	}
}
