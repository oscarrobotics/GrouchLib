package frc.team832.GrouchLib.Mechanisms;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OscarComplexMechanism {

	private OscarSmartMechanism _primaryMech, _secondaryMech;
	List<OscarMechanismComplexPosition> _positions;

	public OscarComplexMechanism(OscarSmartMechanism primaryMech,
	                             OscarSmartMechanism secondaryMech,
	                             OscarMechanismComplexPosition[] positions) {
		_primaryMech = primaryMech;
		_secondaryMech = secondaryMech;
		_positions = Arrays.asList(positions);
	}

	public void setPosition(OscarMechanismComplexPosition position) {
		_primaryMech.setPosition(position.getPrimaryPosition());
		_secondaryMech.setPosition(position.getSecondaryPosition());
	}

	public void setPosition(String index) {
		setPosition(getPosition(index));
	}

	public OscarMechanismComplexPosition getPosition(String index) {
		return _positions.stream()
				.filter(pos -> Objects.equals(index, pos.getIndex()))
				.findFirst()
				.orElse(null);
	}
}
