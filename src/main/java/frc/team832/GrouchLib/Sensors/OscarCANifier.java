package frc.team832.GrouchLib.Sensors;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.GeneralPin;
import frc.team832.GrouchLib.Motors.OscarCANSparkMax;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class OscarCANifier {

	private CANifier _canifier;
	private List<GeneralPin> _inputPins = new ArrayList<>();
	private List<GeneralPin> _outputPins = new ArrayList<>();
	private List<GeneralPin> _pwmPins = new ArrayList<>();

	private CANifier.LEDChannel _ledRChannel = CANifier.LEDChannel.LEDChannelC;
	private CANifier.LEDChannel _ledGChannel = CANifier.LEDChannel.LEDChannelB;
	private CANifier.LEDChannel _ledBChannel = CANifier.LEDChannel.LEDChannelA;

	private double _ledVoltage = 12;
	private double _ledMaxOutput = 1;

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

	public Ultrasonic addUltrasonic(CANifier.PWMChannel triggerPin, CANifier.PWMChannel echoPin) {
		// don't check trigger pin, as we can use the same trigger for multiple ultrasonics
		GeneralPin t_triggerPin = GeneralPinFromPWMChannel(triggerPin);
		GeneralPin t_echoPin = GeneralPinFromPWMChannel(echoPin);
		if (_outputPins.contains(t_echoPin) || _inputPins.contains(t_echoPin)) throw new UnsupportedOperationException("Echo pin is already assigned as a digital channel!");
		if (_pwmPins.contains(t_echoPin)) throw new UnsupportedOperationException("Echo pin is already assigned as a PWM Channel!");

		if (!_pwmPins.contains(t_triggerPin)) {
			_pwmPins.add(t_triggerPin);
		}
		_pwmPins.add(t_echoPin);

		return new Ultrasonic(triggerPin, echoPin, _canifier);
	}

	/* RGB channel assignment is as follows
	 * Red = LEDChannelC
	 * Green = LEDChannelA
	 * Blue = LEDChannelB
	 */

	public void setLedChannels(CANifier.LEDChannel ledRChannel, CANifier.LEDChannel ledGChannel, CANifier.LEDChannel ledBChannel) {
		_ledRChannel = ledRChannel;
		_ledGChannel = ledGChannel;
		_ledBChannel = ledBChannel;
	}

	public void setLedRGB(double rValue, double gValue, double bValue) {
		double trueOutput = (_ledVoltage / 12) * _ledMaxOutput;
		System.out.println("Setting LED Output: " + trueOutput);
		_canifier.setLEDOutput(rValue * trueOutput, _ledRChannel);
		_canifier.setLEDOutput(gValue * trueOutput, _ledGChannel);
		_canifier.setLEDOutput(bValue * trueOutput, _ledBChannel);
	}

	public void setLedColor(Color color) {
		setLedRGB((double)color.getRed() / 256.0, (double)color.getGreen() / 256.0, (double)color.getBlue() / 256.0);
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

	public void setLedVoltage(double volts) {
		_ledVoltage = volts;
	}

	public void setLedMaxOutput(double maxOutput) {
		_ledMaxOutput = maxOutput;
	}

	public static class Ultrasonic {
		private static final double kPingTime = 10 * 1e-6;
		private static final double kSpeedOfSoundInchesPerSec = 1130.0 * 12.0;

		private CANifier _canifier;
		private CANifier.PWMChannel _triggerPin, _echoPin;
		private long lastPing;

		double[] _dutyCycleAndPeriod = new double[]{0, 0};

		public Ultrasonic(CANifier.PWMChannel triggerPin, CANifier.PWMChannel echoPin, CANifier canifier) {
			_triggerPin = triggerPin;
			_echoPin = echoPin;
			_canifier = canifier;
		}

		public void start() {
			_canifier.setPWMOutput(_triggerPin.value, 0.00238095238); // attempt to hit 10us pulse exactly
			_canifier.enablePWMOutput(_triggerPin.value, true);
		}

		public void stop() {
			_canifier.enablePWMOutput(_triggerPin.value, false);
		}

		public void ping() {
			start();
			stop();
		}

		public void update() {
			_canifier.getPWMInput(_echoPin, _dutyCycleAndPeriod);
		}

		public double getMeasuredPulseWidthUs() {
			return _dutyCycleAndPeriod[0];
		}

		public double getPulsePeriod() { return _dutyCycleAndPeriod[1];	}

		private boolean isRangeValid() {
			return _dutyCycleAndPeriod[1] > 1;
		}

		public double getRangeInches() {
			return isRangeValid() ? getMeasuredPulseWidthUs() * 0.0133 / 2.0 : 0;
		}

		public double getRangeMM() {
			return getRangeInches() * 25.4;
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
