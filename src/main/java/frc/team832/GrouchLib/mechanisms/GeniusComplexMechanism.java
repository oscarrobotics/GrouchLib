package frc.team832.GrouchLib.mechanisms;

import frc.team832.GrouchLib.mechanisms.Positions.MechanismComplexPosition;
import frc.team832.GrouchLib.mechanisms.Positions.MechanismComplexPositionList;

public class GeniusComplexMechanism {

	private GeniusMechanism _primaryMech, _secondaryMech;
	MechanismComplexPositionList _positions;

	public GeniusComplexMechanism(GeniusMechanism primaryMech,
	                              GeniusMechanism secondaryMech,
	                              MechanismComplexPositionList positions) {
		_primaryMech = primaryMech;
		_secondaryMech = secondaryMech;
		_positions = positions;
	}

	public void setPosition(MechanismComplexPosition position) {
		_primaryMech.setPosition(position.getPrimaryPosition());
		_secondaryMech.setPosition(position.getSecondaryPosition());
	}

	public void setPosition(String index) {
		setPosition(getPosition(index));
	}

	public GeniusMechanism getPrimaryMechanism(){
		return _primaryMech;
	}

	public GeniusMechanism getSecondaryMechanism(){
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


	public MechanismComplexPosition getPosition(String index) {
		return _positions.getByIndex(index);
		//		return _positions.stream()
//				.filter(pos -> Objects.equals(index, pos.getIndex()))
//				.findFirst()
//				.orElse(null);
	}
}
