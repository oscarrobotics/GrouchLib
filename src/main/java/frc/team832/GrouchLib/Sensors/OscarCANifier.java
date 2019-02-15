package frc.team832.GrouchLib.Sensors;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.GeneralPin;
import frc.team832.GrouchLib.Motors.OscarCANSparkMax;
import frc.team832.GrouchLib.OscarCANDevice;
import frc.team832.GrouchLib.Util.OscarMath;

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

	private double _ledMaxOutput = 1; //

	private boolean onBus;

	public OscarCANifier(int canID) {
		_canifier = new CANifier(canID);

		onBus = !(_canifier.getFirmwareVersion() > 0); // TODO: better way to do this?
		OscarCANDevice.addDevice(new OscarCANDevice(canID, onBus, "CANifier"));
	}

	public int getQuadVelocity() { return _canifier.getQuadratureVelocity(); }
	public int getQuadPosition() { return _canifier.getQuadraturePosition(); }

	public boolean getPinState(GeneralPin pin) {
		return _canifier.getGeneralInput(pin);
	}

	public void setDigitalOutputOn(GeneralPin pin) {
		setDigitalOutput(pin, true);
	}

	public void setDigitalOutputOff(GeneralPin pin) {
		setDigitalOutput(pin, false);
	}

	public void setDigitalOutput(GeneralPin pin, boolean state) {
		if (onBus) {
			_canifier.setGeneralOutput(pin, state, true);
		}
	}

	public Ultrasonic getUltrasonic(CANifier.PWMChannel triggerPin, CANifier.PWMChannel echoPin) {
		return new Ultrasonic(triggerPin, echoPin, this);
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
		if (onBus) {
			System.out.println("Setting LED Output: " + _ledMaxOutput);
			_canifier.setLEDOutput(rValue * _ledMaxOutput, _ledRChannel);
			_canifier.setLEDOutput(gValue * _ledMaxOutput, _ledGChannel);
			_canifier.setLEDOutput(bValue * _ledMaxOutput, _ledBChannel);
		}
	}

	public void setLedColor(Color color) {
		setLedRGB((double)color.getRed() / 256.0, (double)color.getGreen() / 256.0, (double)color.getBlue() / 256.0);
	}

	public void setLedR(double value) {
		value = OscarMath.clip(value, 0, 255);
		setLedRGB(value, 0, 0);
	}

	public void setLedG(double value) {
		value = OscarMath.clip(value, 0, 255);
		setLedRGB(0, value, 0);
	}

	public void setLedB(double value) {
		value = OscarMath.clip(value, 0, 255);
		setLedRGB(0, 0, value);
	}

	public void setLedOff() {
		setLedRGB(0, 0, 0);
	}

	public void setLedMaxOutput(double maxOutput) {
		_ledMaxOutput = OscarMath.clip(maxOutput, 0, 1);
	}

	public static class Ultrasonic {
		private static final double kTriggerPulseTime = 0.00238095238;

		private OscarCANifier _canifier;
		private CANifier.PWMChannel _triggerPin, _echoPin;

		double[] _dutyCycleAndPeriod = new double[]{0, 0};

		public Ultrasonic(CANifier.PWMChannel triggerPin, CANifier.PWMChannel echoPin, OscarCANifier canifier) {
			_triggerPin = triggerPin;
			_echoPin = echoPin;
			_canifier = canifier;
		}

		public void start() {
			_canifier.setPWMOutput(_triggerPin.value, kTriggerPulseTime); // attempt to hit 10us pulse exactly
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
			return isRangeValid() ? getMeasuredPulseWidthUs() * 0.0133 / 2.0 : -1;
		}

		public double getRangeMM() {
			return getRangeInches() * 25.4;
		}
	}

	private void getPWMInput(CANifier.PWMChannel pwmChannel, double[] pulseWidthAndPeriod) {
		if (onBus) {
			_canifier.getPWMInput(pwmChannel, pulseWidthAndPeriod);
		}
	}

	private void enablePWMOutput(int pwmChannel, boolean enable) {
		if (onBus) {
			_canifier.enablePWMOutput(pwmChannel, enable);
		}
	}

	private void setPWMOutput(int pwmChannel, double dutyCycle) {
		if (onBus) {
			_canifier.setPWMOutput(pwmChannel, dutyCycle);
		}
	}
}
