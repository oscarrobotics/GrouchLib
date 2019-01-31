package frc.team832.GrouchLib.Mechanisms.Positions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OscarMechanismComplexPositionList {

	List<OscarMechanismComplexPosition> _presetPositions;

	public OscarMechanismComplexPositionList(OscarMechanismComplexPosition[] presetPositions) {
		_presetPositions = Arrays.asList(presetPositions);
	}

	public OscarMechanismComplexPosition getByIndex(String index) {
		return _presetPositions.stream()
				.filter(pos -> Objects.equals(index, pos.getIndex()))
				.findFirst()
				.orElse(null );
	}
}
