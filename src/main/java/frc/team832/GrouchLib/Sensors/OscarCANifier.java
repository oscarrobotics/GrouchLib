package frc.team832.GrouchLib.Sensors;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.GeneralPin;

import java.awt.*;
import java.util.List;

public class OscarCANifier {

	private CANifier _canifier;
	private List<GeneralPin> _inputPins;
	private List<GeneralPin> _outputPins;
	private List<GeneralPin> _pwmPins;

	public OscarCANifier(CANifier canifier) {
		_canifier = canifier;
	}

	public boolean addDigitalInput(GeneralPin pin) throws UnsupportedOperationException {
		if (_outputPins.contains(pin)) throw new UnsupportedOperationException("Pin is already assigned as an output!");
		if (_pwmPins.contains(pin)) throw new UnsupportedOperationException("Pin is already assigned as a PWM output!");
		if (!_inputPins.contains(pin)) return _inputPins.add(pin);
		else throw new UnsupportedOperationException("Pin is already assigned as an input!");
	}

	public boolean addDigitalOutput(GeneralPin pin) throws UnsupportedOperationException {
		return addDigitalOutput(pin, false);
	}

	public boolean addDigitalOutput(GeneralPin pin, boolean initialState) throws UnsupportedOperationException {
		if (_inputPins.contains(pin)) throw new UnsupportedOperationException("Pin is already assigned as an input!");
		if (_pwmPins.contains(pin)) throw new UnsupportedOperationException("Pin is already assigned as a PWM output!");
		if (!_outputPins.contains(pin)) {
			_canifier.setGeneralOutput(pin, initialState, true);
			return _outputPins.add(pin);
		}
		else throw new UnsupportedOperationException("Pin is already assigned as an output!");
	}

	public boolean getPinState(GeneralPin pin) {
		return _canifier.getGeneralInput(pin);
	}

	public void setDigitalOutputOn(GeneralPin pin) throws UnsupportedOperationException  {
		setDigitalOutput(pin, true);
	}

	public void setDigitalOutputOff(GeneralPin pin) throws UnsupportedOperationException {
		setDigitalOutput(pin, false);
	}

	public void setDigitalOutput(GeneralPin pin, boolean state) throws UnsupportedOperationException {
		if (_inputPins.contains(pin)) throw new UnsupportedOperationException("Pin is assigned as an input!");
		if (_pwmPins.contains(pin)) throw new UnsupportedOperationException("Pin is already assigned as a PWM output!");
		if (!_outputPins.contains(pin)) throw new UnsupportedOperationException("Pin is not assigned as an output!");
		else {
			_canifier.setGeneralOutput(pin, state, true);
		}
	}

	/* RGB channel assignment is as follows
	 * Red = LEDChannelA
	 * Blue = LEDChannelB
	 * Green = LEDChannelC
	 */

	public void setLedRGB(double rValue, double gValue, double bValue) {
		_canifier.setLEDOutput(rValue, CANifier.LEDChannel.LEDChannelA);
		_canifier.setLEDOutput(gValue, CANifier.LEDChannel.LEDChannelB);
		_canifier.setLEDOutput(bValue, CANifier.LEDChannel.LEDChannelC);
	}

	public void setLedColor(Color color) {
		setLedRGB(color.getRed(), color.getGreen(), color.getBlue());
	}

	public void setLedR(double value) {
		setLedRGB(value, 0, 0);
	}

	public void setLedG(double value) {
		setLedRGB(0, value, 0);
	}

	public void setLedB(double value) {
		setLedRGB(0, 0, value);
	}

	public void setLedOff() {
		setLedRGB(0, 0, 0);
	}

}
