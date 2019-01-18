package frc.team832.GrouchLib.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team832.GrouchLib.Motors.IOscarSmartMotor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class OscarSmartMechanism {

	private IOscarSmartMotor m_smartMotor;
	private List<OscarMechanismPosition> _presetPositions;

	public OscarSmartMechanism(IOscarSmartMotor smartMotor, OscarMechanismPosition[] presetPositions) {
		m_smartMotor = smartMotor;
		_presetPositions = Arrays.asList(presetPositions);
	}

	public double getPosition() {
		return m_smartMotor.getPosition();
	}

	public void setPosition(OscarMechanismPosition position) {
		m_smartMotor.setMode(ControlMode.Position);
		m_smartMotor.set(position.getTarget());
	}

	public void setPosition(String index) {
		setPosition(getPosition(index));
	}

	public OscarMechanismPosition getPosition(String index) {
		return _presetPositions.stream()
				.filter(pos -> Objects.equals(index, pos.getIndex()))
				.findFirst()
				.orElse(null );
	}

	public void stop() {
		m_smartMotor.stopMotor();
	}
}
