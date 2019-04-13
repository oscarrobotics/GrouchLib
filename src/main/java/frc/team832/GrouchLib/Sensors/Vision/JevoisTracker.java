package frc.team832.GrouchLib.Sensors.Vision;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;

public class JevoisTracker extends VisionTracker implements Runnable {

	private static final String TapePrefix = "TAPE";

	private UsbCamera _jevoisCam;
	private Thread _stream;
	private SerialPort _jevoisPort;

	private String[] parts;
	private boolean detectsTape;

	public JevoisTracker(SerialPort.Port port, int baud) {
		_stream = new Thread(this);
		try {
			_jevoisPort = new SerialPort(baud, port);
			_stream.start();
		} catch (Exception e) {
			DriverStation.reportError("Jevois Cam Serial port missing!", e.getStackTrace());
		}
	}

	public void startCameraStream() {
		if (_stream.isAlive()) {
			try {
				_jevoisCam = CameraServer.getInstance().startAutomaticCapture();
				_jevoisCam.setVideoMode(VideoMode.PixelFormat.kYUYV, 320, 240, 30);
			} catch (Exception e) {
				DriverStation.reportError("Jevois Cam failed to connect!", e.getStackTrace());
			}
		}
	}

	public boolean detectsTape() {
		return detectsTape;
	}

	@Override
	public void run() {
		while (_stream.isAlive()) {
			Timer.delay(0.01);
			try {
				if (_jevoisPort.getBytesReceived() > 0) {
					String read = _jevoisPort.readString();
					if (read.startsWith(TapePrefix)) {
						detectsTape = true;
						parts = dataParse(read);
					} else {
						detectsTape = false;

					}
				}
			} catch (Exception e) {
				DriverStation.reportError("Jevois Cam Serial failed to connect!", e.getStackTrace());
				detectsTape = false;
			}
		}
	}

	private String[] dataParse(String input) {
		input = input.substring(TapePrefix.length() + 1); // strip prefix from front (including single whitespace
		input = input.stripLeading(); // strip whitespace at front
		return input.split("\\s*,\\s*");
	}

	public String[] getParts() {
		return parts;
	}
}
