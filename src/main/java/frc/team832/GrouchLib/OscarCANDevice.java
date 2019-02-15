package frc.team832.GrouchLib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OscarCANDevice {
	private static HashMap<Integer, OscarCANDevice> _canDevices = new HashMap<>();

	private int _id;
	private boolean _onBus;
	private String _deviceName;

	public OscarCANDevice(int id, boolean foundOnBus, String deviceName){
		_id = id;
		_onBus = foundOnBus;
		_deviceName = deviceName;
	}

	public static void addDevice(OscarCANDevice device) {
		_canDevices.putIfAbsent(device._id, device);
	}

	/**
	 * Indicates whether or not any devices are missing from the CAN Bus
	 * @return boolean
	 */
	public static boolean hasMissingDevices() {
		return getDevices().stream().anyMatch(device -> !device._onBus);
	}

	public static List<OscarCANDevice> getDevices() {
		return new ArrayList<>(_canDevices.values());
	}

	public static String getDeviceDescription(int id) {
		return _canDevices.get(id).toString();
	}

	@Override
	public String toString() {
		return String.format("Type: %12s. ID: %2d. On Bus: %5b.", _deviceName, _id, _onBus);
	}
}
