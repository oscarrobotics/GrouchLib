package frc.team832.GrouchLib.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.team832.GrouchLib.Motors.IOscarSmartMotor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class OscarSmartMechanism {

	private IOscarSmartMotor m_smartMotor;
	private List<OscarMechanismPosition> _presetPositions = new ArrayList<>();

	public OscarSmartMechanism(IOscarSmartMotor smartMotor) {
		m_smartMotor = smartMotor;
	}

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

	/**
	 * Set mechanism position by encoder value
	 * @param position
	 */
	public void setRawPosition(double position) {
		m_smartMotor.setMode(ControlMode.Position);
		m_smartMotor.set(position);
	}

	public boolean addPosition(OscarMechanismPosition position) {
		boolean hasIndex = _presetPositions.stream()
				.anyMatch(pos -> Objects.equals(position.getIndex(), pos.getIndex()));
		if (hasIndex) {
			return false;
		}
		return _presetPositions.add(position);
	}

	public OscarMechanismPosition getPosition(String index) {
		return _presetPositions.stream()
				.filter(pos -> Objects.equals(index, pos.getIndex()))
				.findFirst()
				.orElse(null );
	}

	public abstract double getPositionInches();

	public abstract void setPositionInches();

	public void stop() {
		m_smartMotor.stopMotor();
	}
}
