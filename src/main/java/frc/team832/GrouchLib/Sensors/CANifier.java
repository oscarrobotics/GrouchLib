package frc.team832.GrouchLib.Sensors;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.GeneralPin;
import edu.wpi.first.wpilibj.Timer;
import frc.team832.GrouchLib.CANDevice;
import frc.team832.GrouchLib.Util.OscarMath;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sin;

public class CANifier {

	private com.ctre.phoenix.CANifier _canifier;
	private List<GeneralPin> _inputPins = new ArrayList<>();
	private List<GeneralPin> _outputPins = new ArrayList<>();
	private List<GeneralPin> _pwmPins = new ArrayList<>();

	private boolean onBus;

	public CANifier(int canID) {
		_canifier = new com.ctre.phoenix.CANifier(canID);

		onBus = _canifier.getBusVoltage() > 0;
		CANDevice.addDevice(new CANDevice(canID, onBus, "CANifier"));
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

	public Ultrasonic getUltrasonic(com.ctre.phoenix.CANifier.PWMChannel triggerPin, com.ctre.phoenix.CANifier.PWMChannel echoPin) {
		return new Ultrasonic(triggerPin, echoPin, this);
	}

	private void getPWMInput(com.ctre.phoenix.CANifier.PWMChannel pwmChannel, double[] pulseWidthAndPeriod) {
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

	public enum LEDMode {
		STATIC,
		ALTERNATE_GREEN,
		ALTERNATE_2,
		FADE,
		FADE_2,
		RAINBOW
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

	public class LEDs {
		private LEDMode ledMode;
		private Color curColor;
		private Color color1, color2, color3;

		private CANifier.LEDChannel _rChannel = CANifier.LEDChannel.LEDChannelC;
		private CANifier.LEDChannel _gChannel = CANifier.LEDChannel.LEDChannelB;
		private CANifier.LEDChannel _bChannel = CANifier.LEDChannel.LEDChannelA;
		private double _maxOutput = 1;

		public void setLedChannels(CANifier.LEDChannel ledRChannel, CANifier.LEDChannel ledGChannel, CANifier.LEDChannel ledBChannel) {
			_rChannel = ledRChannel;
			_gChannel = ledGChannel;
			_bChannel = ledBChannel;
		}

//		public void setRGB(int rValue, int gValue, int bValue) {
//			rValue = OscarMath.clip(rValue, 0, 255);
//			gValue = OscarMath.clip(gValue, 0, 255);
//			bValue = OscarMath.clip(bValue, 0, 255);
//			color1 = new Color(rValue, gValue, bValue);
//		}

		public void setLEDs(LEDMode mode, Color color) {
			setMode(mode);
			setPrimaryColor(color);
		}

		public void setMode(LEDMode mode) {
			ledMode = mode;
		}
    
		public void setPrimaryColor(Color color) {
			color1 = color;
		}
		public void setSecondaryColor(Color color) { color2 = color; }
		public void setTertiaryColor(Color color) { color3 = color; }

		private void sendColor(int r, int g, int b) {
			sendColor(new Color(r, g, b));
		}

		private void sendColor(Color color) {
			double[] vals = ColorToPercentRGB(color);
			if (onBus) {
				_canifier.setLEDOutput(vals[0] * _maxOutput, _rChannel);
				_canifier.setLEDOutput(vals[1] * _maxOutput, _gChannel);
				_canifier.setLEDOutput(vals[2] * _maxOutput, _bChannel);
			}
		}

		private double[] ColorToPercentRGB(Color color) {
			return new double[]{
					color.getRed() / 255D,
					color.getGreen() / 255D,
					color.getBlue() / 255D
			};
		}

		private double[] RGBToPercentRGB(int r, int g, int b) {
			return new double[]{
					r / 255D,
					g / 255D,
					b / 255D};
		}

		public void turnOff() {
			sendColor(Color.black);
		}

		public void setMaxOutput(double maxOutput) {
			_maxOutput = OscarMath.clip(maxOutput, 0, 1);
		}

		private class LEDRunner implements Runnable {
			@Override
			public void run() {
				int brightness = 0;
				int fadeAmount = 3;

				Timer timer = new Timer();
				timer.start();
				while (true) {
					switch (ledMode) {
						case STATIC:
							sendColor(curColor);
							break;
						case ALTERNATE_GREEN:
							if(timer.hasPeriodPassed(0.125)) {
								Color newColor = curColor != Color.green ? color1 : Color.green;
								sendColor(newColor);
							}
							break;
						case FADE:
							if (timer.hasPeriodPassed(0.125)) {
								brightness = brightness + fadeAmount;

								// reverse the direction of the fading at the ends of the fade:
								if (brightness <= 0 || brightness >= 255) {
									fadeAmount = -fadeAmount;

								}
								sendColor(new Color(curColor.getRed() - brightness, curColor.getGreen() - brightness, curColor.getRed() - brightness));

							}
							break;
						case FADE_2:

						case RAINBOW:
							if (timer.hasPeriodPassed(0.05)) {
								int n = 256; // number of steps
								float TWO_PI = (3.14159f * 2);

								for (int i = 0; i < n; ++i) {
									int red = (int) (128 + sin(i * TWO_PI / n + 0) + 127);
									int grn = (int) (128 + sin(i * TWO_PI / n + TWO_PI / 3) + 127);
									int blu = (int) (128 + sin(i * TWO_PI / n + 2 * TWO_PI / 3) + 127);
									sendColor(new Color(red, grn, blu));
								}
							}
							break;
					}
				}
			}
		}
	}
}
