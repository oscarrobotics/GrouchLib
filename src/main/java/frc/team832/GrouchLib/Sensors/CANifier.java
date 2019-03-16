package frc.team832.GrouchLib.Sensors;

import com.ctre.phoenix.CANifier.GeneralPin;
import edu.wpi.first.wpilibj.Notifier;
import frc.team832.GrouchLib.CANDevice;
import frc.team832.GrouchLib.Util.OscarMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"})
public class CANifier {

	private com.ctre.phoenix.CANifier _canifier;
	private List<GeneralPin> _inputPins = new ArrayList<>();
	private List<GeneralPin> _outputPins = new ArrayList<>();
	private List<GeneralPin> _pwmPins = new ArrayList<>();

	private boolean onBus;
	private RemoteEncoder quadEnc;

	public CANifier(int canID) {
		_canifier = new com.ctre.phoenix.CANifier(canID);

		onBus = _canifier.getBusVoltage() > 0;
		CANDevice.addDevice(new CANDevice(canID, onBus, "CANifier"));
	}

	public int getQuadVelocity() { return onBus ? _canifier.getQuadratureVelocity() : -1; }

	public int getQuadPosition() { return onBus ? _canifier.getQuadraturePosition() : -1; }

	public boolean getPinState(GeneralPin pin) { return onBus && _canifier.getGeneralInput(pin); }

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

	public Ultrasonic getUltrasonic(com.ctre.phoenix.CANifier.PWMChannel triggerPin, com.ctre.phoenix.CANifier.PWMChannel echoPin) {
		return new Ultrasonic(triggerPin, echoPin, this);
	}


	public void getPWMInput(com.ctre.phoenix.CANifier.PWMChannel pwmChannel, double[] pulseWidthAndPeriod) {
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

	public static class Ultrasonic {
		private static final double kTriggerPulseTime = 0.00238095238;
		double[] _dutyCycleAndPeriod = new double[]{0, 0};
		private CANifier _canifier;
		private com.ctre.phoenix.CANifier.PWMChannel _triggerPin, _echoPin;

		public Ultrasonic(com.ctre.phoenix.CANifier.PWMChannel triggerPin, com.ctre.phoenix.CANifier.PWMChannel echoPin, CANifier canifier) {
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

		public void update() {
			_canifier.getPWMInput(_echoPin, _dutyCycleAndPeriod);
		}

		public double getMeasuredPulseWidthUs() {
			return _dutyCycleAndPeriod[0];
		}

		public double getPulsePeriod() { return _dutyCycleAndPeriod[1]; }

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

	private com.ctre.phoenix.CANifier.LEDChannel _rChannel = com.ctre.phoenix.CANifier.LEDChannel.LEDChannelA;
	private com.ctre.phoenix.CANifier.LEDChannel _gChannel = com.ctre.phoenix.CANifier.LEDChannel.LEDChannelB;
	private com.ctre.phoenix.CANifier.LEDChannel _bChannel = com.ctre.phoenix.CANifier.LEDChannel.LEDChannelC;
	private double _maxOutput = 1;
	private LEDRunner _ledRunner;
	private Notifier ledNotifier;

	private Color _lastColor;

	public void initLEDs(LEDRunner ledRunner) {
		_ledRunner = ledRunner;
		ledNotifier = new Notifier(_ledRunner);
	}

	public void startLEDs() {
		try {
			ledNotifier.startPeriodic(0.02);
		} catch (Exception e) {
			System.out.println("No Runnable set for LEDs!");
		}
	}

	public void stopLEDs() {
		ledNotifier.stop();
	}

	public void setLEDs(Color color) {
		if (_ledRunner != null)
			_ledRunner.setColor(color);
	}

	public void turnOff() {
		if (_ledRunner != null)
			_ledRunner.setOff();
	}

	// Setters
	public void setLedChannels(com.ctre.phoenix.CANifier.LEDChannel ledRChannel, com.ctre.phoenix.CANifier.LEDChannel ledGChannel, com.ctre.phoenix.CANifier.LEDChannel ledBChannel) {
		_rChannel = ledRChannel;
		_gChannel = ledGChannel;
		_bChannel = ledBChannel;
	}

	public void setLedMaxOutput(double maxOutput) {
		_maxOutput = OscarMath.clip(maxOutput, 0, 1);
	}

	public void sendColor(Color color) {
		if (color == null) color = Color.BLACK;
		if (_lastColor == color) {
			return;
		}
		_lastColor = color;
		double[] vals = ColorToPercentRGB(color);
		if (onBus) {
			_canifier.setLEDOutput(vals[0] * _maxOutput, _rChannel);
			_canifier.setLEDOutput(vals[1] * _maxOutput, _gChannel);
			_canifier.setLEDOutput(vals[2] * _maxOutput, _bChannel);
		}
	}

	public void sendHSB(float[] hsbVals) {
		sendHSB(hsbVals[0], hsbVals[1], hsbVals[2]);
	}

	public void sendHSB(float hue, float sat, float bri) {
		int colInt = Color.HSBtoRGB(hue, sat, bri);
		Color col = new Color(colInt);
		sendColor(col);
	}

	private double[] ColorToPercentRGB(Color color) {
		return new double[]{
				color.getRed() / 255D,
				color.getGreen() / 255D,
				color.getBlue() / 255D
		};
	}

	private double[] RGBToPercentRGB(int r, int g, int b) {
		r = OscarMath.clip(r, 0, 255);
		g = OscarMath.clip(g, 0, 255);
		b = OscarMath.clip(b, 0, 255);
		return new double[]{
				r / 255D,
				g / 255D,
				b / 255D};
	}

	public interface LEDMode {
		// nothin
	}

	public void setLEDs(LEDMode ledMode, Color color) {
		if (_ledRunner != null) {
			_ledRunner.setColor(color);
			_ledRunner.setMode(ledMode);
		}
	}

	public static abstract class LEDRunner implements Runnable {
		public abstract void setColor(Color color);
		public abstract void setMode(LEDMode ledMode);
		public abstract void setOff();
	}
}
