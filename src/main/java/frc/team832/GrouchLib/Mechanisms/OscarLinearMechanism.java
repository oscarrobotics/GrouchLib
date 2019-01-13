package frc.team832.GrouchLib.Mechanisms;

import frc.team832.GrouchLib.Motors.IOscarSmartMotor;
import com.ctre.phoenix.motorcontrol.ControlMode;

import java.util.ArrayList;
import java.util.List;

public class OscarLinearMechanism {

    private IOscarSmartMotor m_linearMotor;
    private List<Position> _presetPositions = new ArrayList<>();

    public OscarLinearMechanism(IOscarSmartMotor linearMotor) {
        m_linearMotor = linearMotor;
    }

    public double getPosition() {
        return m_linearMotor.getPosition();
    }

    public void setPosition(double position) {
        m_linearMotor.setMode(ControlMode.Position);
        m_linearMotor.set(position);
    }

    public boolean addPosition(Position position) {
        boolean hasIndex = _presetPositions.stream()
                .anyMatch(pos -> position._index.equals(pos._index));
        if (hasIndex) {
            return false;
        }
        return _presetPositions.add(position);
    }

    public Position getPosition(String index) {
        return _presetPositions.stream()
                .filter(pos -> index.equals(pos._index))
                .findAny()
                .orElse(null );
    }

    public void stop() {
        m_linearMotor.stopMotor();
    }

    public static class Position {
        private String _index;
        private int _position;

        public Position(String index, int position) {
            _index = index;
            _position = position;
        }
    }
}
