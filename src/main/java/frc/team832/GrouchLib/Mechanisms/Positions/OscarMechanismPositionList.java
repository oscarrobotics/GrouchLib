package frc.team832.GrouchLib.Mechanisms.Positions;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OscarMechanismPositionList {

	List<OscarMechanismPosition> _presetPositions;

	public OscarMechanismPositionList(OscarMechanismPosition[] presetPositions) {
		_presetPositions = Arrays.asList(presetPositions);
	}

	public OscarMechanismPosition getByIndex(String index) {
		return _presetPositions.stream()
				.filter(pos -> Objects.equals(index, pos.getIndex()))
				.findFirst()
				.orElse(null );
	}
}
