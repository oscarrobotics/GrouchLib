package frc.team832.GrouchLib.Mechanisms.Positions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MechanismComplexPositionList {

	List<MechanismComplexPosition> _presetPositions;

	public MechanismComplexPositionList(MechanismComplexPosition[] presetPositions) {
		_presetPositions = Arrays.asList(presetPositions);
	}

	public MechanismComplexPosition getByIndex(String index) {
		return _presetPositions.stream()
				.filter(pos -> Objects.equals(index, pos.getIndex()))
				.findFirst()
				.orElse(null );
	}
}
