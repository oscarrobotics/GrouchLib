package frc.team832.lib.sensors;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.GeneralPin;
import edu.wpi.first.wpilibj.Notifier;
import frc.team832.lib.CANDevice;
import frc.team832.lib.util.OscarMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"})
public class OscarCANifier {

	private final CANifier _canifier;
	private final List<GeneralPin> _inputPins = new ArrayList<>();
	private final List<GeneralPin> _outputPins = new ArrayList<>();
	private final List<GeneralPin> _pwmPins = new ArrayList<>();

	private final boolean onBus;

	public OscarCANifier(int canID) {
		_canifier = new CANifier(canID);

		onBus = _canifier.getBusVoltage() > 0;
		CANDevice.addDevice(canID, onBus, "CANifier");
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

	public Ultrasonic getUltrasonic(CANifier.PWMChannel triggerPin, CANifier.PWMChannel echoPin) {
		return new Ultrasonic(triggerPin, echoPin, this);
	}


	public void getPWMInput(CANifier.PWMChannel pwmChannel, double[] pulseWidthAndPeriod) {
		if (onBus) {
			_canifier.getPWMInput(pwmChannel, pulseWidthAndPeriod);
		}
	}

	private void enablePWMOutput(int pwmChannel, boolean enable) {
		if (onBus) {
			_canifier.enablePWMOutput(pwmChannel, enable);
		}
	}

	@SuppressWarnings("SameParameterValue")
	private void setPWMOutput(int pwmChannel, double dutyCycle) {
		if (onBus) {
			_canifier.setPWMOutput(pwmChannel, dutyCycle);
		}
	}

	public static class Ultrasonic {
		private static final double kTriggerPulseTime = 0.00238095238;
		double[] _dutyCycleAndPeriod = new double[]{0, 0};
		private final OscarCANifier _canifier;
		private final CANifier.PWMChannel _triggerPin;
		private final CANifier.PWMChannel _echoPin;

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

	private CANifier.LEDChannel _rChannel = CANifier.LEDChannel.LEDChannelA;
	private CANifier.LEDChannel _gChannel = CANifier.LEDChannel.LEDChannelB;
	private CANifier.LEDChannel _bChannel = CANifier.LEDChannel.LEDChannelC;
	private double _maxOutput = 1;
	private LEDRunner _ledRunner;
	private Notifier ledNotifier;

	private Color _lastColor;
	private double _lastMaxOutput;

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
	public void setLedChannels(CANifier.LEDChannel ledRChannel, CANifier.LEDChannel ledGChannel, CANifier.LEDChannel ledBChannel) {
		_rChannel = ledRChannel;
		_gChannel = ledGChannel;
		_bChannel = ledBChannel;
	}

	public void setLedMaxOutput(double maxOutput) {
		_maxOutput = OscarMath.clip(maxOutput, 0, 1);
	}

	public void sendColor(Color color) {
		if (color == null) color = Color.BLACK;
		if (_lastColor == color && _lastMaxOutput == _maxOutput) {
			return;
		}
		_lastColor = color;
		_lastMaxOutput = _maxOutput;
		double[] values = ColorToPercentRGB(color);
		if (onBus) {
			_canifier.setLEDOutput(values[0] * _maxOutput, _rChannel);
			_canifier.setLEDOutput(values[1] * _maxOutput, _gChannel);
			_canifier.setLEDOutput(values[2] * _maxOutput, _bChannel);
		}
	}

	public void sendHSB(float[] hsbValues) {
		sendHSB(hsbValues[0], hsbValues[1], hsbValues[2]);
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
		// nothing
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
