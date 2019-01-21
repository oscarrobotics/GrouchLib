package frc.team832.GrouchLib.Sensors;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.GeneralPin;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.List;

public class OscarCANifier {

	private CANifier _canifier;
	private List<GeneralPin> _inputPins;
	private List<GeneralPin> _outputPins;
	private List<GeneralPin> _pwmPins;

	public OscarCANifier(CANifier canifier) {
		_canifier = canifier;
	}

	public OscarCANifier(int canID) {
		this(new CANifier(canID));
	}

	public boolean addDigitalInput(GeneralPin pin) throws UnsupportedOperationException {
		if (_outputPins.contains(pin)) throw new UnsupportedOperationException("Pin is already assigned as an output!");
		if (_pwmPins.contains(pin)) throw new UnsupportedOperationException("Pin is already assigned as a PWM Channel!");
		if (!_inputPins.contains(pin)) return _inputPins.add(pin);
		else throw new UnsupportedOperationException("Pin is already assigned as an input!");
	}

	public boolean addDigitalOutput(GeneralPin pin) throws UnsupportedOperationException {
		return addDigitalOutput(pin, false);
	}

	public boolean addDigitalOutput(GeneralPin pin, boolean initialState) throws UnsupportedOperationException {
		if (_inputPins.contains(pin)) throw new UnsupportedOperationException("Pin is already assigned as an input!");
		if (_pwmPins.contains(pin)) throw new UnsupportedOperationException("Pin is already assigned as a PWM Channel!");
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
		if (_pwmPins.contains(pin)) throw new UnsupportedOperationException("Pin is already assigned as a PWM Channel!");
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

	public class Ultrasonic {
		private CANifier.PWMChannel _triggerPin, _echoPin;

		double[] _dutyCycleAndPeriod = new double[]{0, 0};

		public Ultrasonic(CANifier.PWMChannel triggerPin, CANifier.PWMChannel echoPin) {
			GeneralPin t_triggerPin = GeneralPinFromPWMChannel(triggerPin);
			GeneralPin t_echoPin = GeneralPinFromPWMChannel(echoPin);
			if (_outputPins.contains(t_triggerPin) || _inputPins.contains(t_triggerPin)) throw new UnsupportedOperationException("Trigger pin is already assigned as a digital channel!");
			if (_outputPins.contains(t_echoPin) || _inputPins.contains(t_echoPin)) throw new UnsupportedOperationException("Echo pin is already assigned as a digital channel!");
			if (_pwmPins.contains(t_triggerPin)) throw new UnsupportedOperationException("Trigger pin is already assigned as a PWM Channel!");
			if (_pwmPins.contains(t_echoPin)) throw new UnsupportedOperationException("Echo pin is already assigned as a PWM Channel!");

			_pwmPins.add(t_triggerPin);
			_pwmPins.add(t_echoPin);
		}

		public void start() {
			_canifier.setPWMOutput(_triggerPin.value, 0.00238095238); // attempt to hit 10us pulse exactly
			_canifier.enablePWMOutput(_triggerPin.value, true);
		}

		public void stop() {
			_canifier.enablePWMOutput(_triggerPin.value, false);
		}

		public void update() {
			start();
			_canifier.getPWMInput(_echoPin, _dutyCycleAndPeriod);
			stop();
		}

		public double getMeasuredPulseWidthUs() {
			update();
			return _dutyCycleAndPeriod[0];
		}
	}

	private static GeneralPin GeneralPinFromPWMChannel(CANifier.PWMChannel channel) {
		switch (channel) {
			case PWMChannel0:
				return GeneralPin.SPI_CLK_PWM0P;
			case PWMChannel1:
				return GeneralPin.SPI_MOSI_PWM1P;
			case PWMChannel2:
				return GeneralPin.SPI_MISO_PWM2P;
			case PWMChannel3:
				return GeneralPin.SPI_CS;
			default:
				throw new IllegalArgumentException("Pin \"" + channel.toString() + "\" is not a PWM Channel!");
		}
	}

	private static CANifier.PWMChannel PWMChannelFromGeneralPin(GeneralPin pin) {
		switch (pin) {
			case SPI_CS: // PWM 3
				return CANifier.PWMChannel.PWMChannel3;
			case SPI_MISO_PWM2P: // PWM2
				return CANifier.PWMChannel.PWMChannel2;
			case SPI_MOSI_PWM1P: // PWM1
				return CANifier.PWMChannel.PWMChannel1;
			case SPI_CLK_PWM0P: // PWM0
				return CANifier.PWMChannel.PWMChannel0;
			default:
				throw new IllegalArgumentException("Pin \"" + pin.toString() + "\" is not a PWM Channel!");
		}
	}
}
