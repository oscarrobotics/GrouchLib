package frc.team832.GrouchLib.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPosition;
import frc.team832.GrouchLib.Mechanisms.Positions.OscarMechanismPositionList;
import frc.team832.GrouchLib.Motors.IOscarSmartMotor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class OscarSmartMechanism {

	private IOscarSmartMotor _smartMotor;
	private OscarMechanismPositionList _presetPositions;

	public OscarSmartMechanism(IOscarSmartMotor smartMotor, OscarMechanismPositionList presetPositions) {
		_smartMotor = smartMotor;
		_presetPositions = presetPositions;
	}

	public double getTargetPosition() { return _smartMotor.getTargetPosition(); }

	public double getCurrentPosition() {
		return _smartMotor.getCurrentPosition();
	}

	public OscarMechanismPosition getPresetPosition(String index) {
		return _presetPositions.getByIndex(index);
	}

	public void setPosition(OscarMechanismPosition position) {
		_smartMotor.setMode(ControlMode.Position);
		_smartMotor.set(position.getTarget());
	}

	public void setPosition(String index) {
		setPosition(getPresetPosition(index));
	}

	public void stop() {
		_smartMotor.stopMotor();
	}
}
