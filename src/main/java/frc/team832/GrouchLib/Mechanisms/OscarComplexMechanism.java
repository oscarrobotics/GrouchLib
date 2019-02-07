package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismComplexPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismComplexPositionList;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class OscarComplexMechanism {

	private OscarSmartMechanism _primaryMech, _secondaryMech;
	OscarMechanismComplexPositionList _positions;

	public OscarComplexMechanism(OscarSmartMechanism primaryMech,
								 OscarSmartMechanism secondaryMech,
								 OscarMechanismComplexPositionList positions) {
		_primaryMech = primaryMech;
		_secondaryMech = secondaryMech;
		_positions = positions;
	}

	public void setPosition(OscarMechanismComplexPosition position) {
		_primaryMech.setPosition(position.getPrimaryPosition());
		_secondaryMech.setPosition(position.getSecondaryPosition());
	}

	public void setPosition(String index) {
		setPosition(getPosition(index));
	}

	public OscarSmartMechanism getPrimaryMechanism(){
		return _primaryMech;
	}

	public OscarSmartMechanism getSecondaryMechanism(){
		return _secondaryMech;
	}

	public double getPrimaryPosition(){
		return _primaryMech.getCurrentPosition();
	}

	public double getPrimaryTarget(){
		return _primaryMech.getTargetPosition();
	}

	public boolean primaryAtTarget(){
		return _primaryMech.getAtTarget();
	}

	public double getSecondaryPosition(){
		return _secondaryMech.getCurrentPosition();
	}

	public double getSecondaryTarget(){
		return _secondaryMech.getTargetPosition();
	}

	public boolean secondaryAtTarget(){
		return _secondaryMech.getAtTarget();
	}


	public OscarMechanismComplexPosition getPosition(String index) {
		return _positions.getByIndex(index);
		//		return _positions.stream()
//				.filter(pos -> Objects.equals(index, pos.getIndex()))
//				.findFirst()
//				.orElse(null);
	}
}
