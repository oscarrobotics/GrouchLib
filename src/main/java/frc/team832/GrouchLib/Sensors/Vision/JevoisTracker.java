package frc.team832.GrouchLib.Sensors.Vision;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;

import java.util.ArrayList;

@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"})
public class JevoisTracker extends VisionTracker implements Runnable {
	private UsbCamera _jevoisCam;
	private Thread _stream;
	private SerialPort _jevoisPort;
	private int _baud;
	private boolean _send;

	public double _offset;
	public double _distinguish = 0.0;
	private double _threshold = 25.4;

	private String[] parts;
	private String sendValue;

	private VisionConstants _constants;

	private double m_contourNum, m_area, m_centerX, m_centerY, m_distance;

	public JevoisTracker(SerialPort port, int baud, VisionConstants constants) {
		detectedObjects = new ArrayList<>();
		_send = false;
		_constants = constants;
		_stream = new Thread(this);
		_stream.start();
	}

	public void startCameraStream() {
		try {
			_jevoisCam = CameraServer.getInstance().startAutomaticCapture();
			_jevoisCam.setVideoMode(VideoMode.PixelFormat.kMJPEG, 320, 240, 30);
		} catch (Exception e) {
			DriverStation.reportError("Jevois Cam failed to connect!", e.getStackTrace());
		}
	}

	@Override
	public void run() {
		while (_stream.isAlive()) {
			Timer.delay(0.001);
			if (_send) {
				_jevoisPort.writeString(sendValue);
				_send = false;
			}
			if (_jevoisPort.getBytesReceived() > 0) {
				String read = _jevoisPort.readString();
				if (read.charAt(0) == '/') {
					parts = dataParse(read);
					m_contourNum = Integer.parseInt(getData(1));
					m_area = Double.parseDouble(getData(2));
					m_centerX = Double.parseDouble(getData(3));
					m_centerY = Double.parseDouble(getData(4));
					m_distance = Double.parseDouble(getData(5));
				} else {
					System.out.println(read);
				}
			}
		}
	}

	// use if jevois is centered on robot
	public double getSimpleAngleToTurn() {
		return xPixelToDegree(getTargetX());
	}

	private double xPixelToDegree(double pixel) {
		double radian = Math.signum(pixel) * Math.atan(Math.abs(pixel / _constants.getXFocalLength()));
		return 180 / Math.PI * radian;
	}

	private double yPixelToDegree(double pixel) {
		double radian = Math.signum(pixel) * Math.atan(Math.abs(pixel / _constants.getXFocalLength()));
		return 180 / Math.PI * radian;
	}

	public double getAngleToTurn(){
		double radians = (Math.PI / 180) * (xPixelToDegree(getTargetX()) + _constants.getCameraHorizontalMountAngle());
		double horizontalAngle = Math.PI / 2 - radians;
		double distance = getDistance();
		double f = Math.sqrt(distance * distance + Math.pow(_constants.getCameraHorizontalOffset(), 2) - 2 * distance * _constants.getCameraHorizontalOffset() * Math.cos(horizontalAngle));
		double c= Math.asin(_constants.getCameraHorizontalOffset() * Math.sin(horizontalAngle) / f);
		double b = Math.PI - horizontalAngle - c;
		double calculatedAngle = (180 / Math.PI) * (Math.PI / 2 - b);
		if (getTargetX() == 0) {
			return 0;
		} else {
			return -calculatedAngle;
		}
	}

	public double getDistance() {
		return m_distance;
	}

	public double getDistanceFeet() {
		return m_distance / 12;
	}

	// use if overshooting the turn
	public double getOffset() {
		double angularError = getSimpleAngleToTurn();
		if (-_threshold < angularError && angularError < _threshold) {
			_offset = 0.0;
		} else if (_threshold > 0) {
			_offset = angularError - _threshold;
		} else if (_threshold < 0) {
			_offset = angularError + _threshold;
		}
		return _offset;
	}

	public double getContourNum() {
		return m_contourNum;
	}

	public double getTargetX() {
		return m_centerX;
	}

	public double getTargetY() {
		return m_centerY;
	}

	public double getTargetArea() {
		return m_area;
	}

	public void end() {
		_stream.interrupt();
	}

	private void sendCommand(String value) {
		sendValue = value + "\n";
		_send = true;
		Timer.delay(0.1);
	}

	private String[] dataParse(String input) {
		return input.split("/");
	}

	private String getData(int data) {
		return parts[data];
	}

	public void ping() {
		sendCommand("ping");
	}

	public void enableStream() {
		sendCommand("streamon");
	}

	public void disableStream() {
		sendCommand("streamoff");
	}
}
