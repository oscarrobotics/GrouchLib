package frc.team832.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import frc.team832.lib.motorcontrol.SmartMC;

public class CANDevice {
	private static final HashMap<Integer, CANDevice> _canDevices = new HashMap<>();

	private final int _id;
	private final boolean _onBus;
	private final String _deviceName;

	protected CANDevice(int id, boolean foundOnBus, String deviceName){
		_id = id;
		_onBus = foundOnBus;
		_deviceName = deviceName;
	}

	public static void addDevice(SmartMC<?> mc, String name) {
		var device = new CANDevice(mc.getCANID(), mc.getCANConnection(), name);
		_canDevices.putIfAbsent(device._id, device);
	}

	public static void addDevice(int id, boolean onBus, String deviceName) {
		var device = new CANDevice(id, onBus, deviceName);
		_canDevices.putIfAbsent(device._id, device);
	}

	/**
	 * Indicates whether or not any devices are missing from the CAN Bus
	 * @return boolean
	 */
	public static boolean hasMissingDevices() {
		return getDevices().stream().anyMatch(device -> !device._onBus);
	}

	public static List<CANDevice> getDevices() {
		return new ArrayList<>(_canDevices.values());
	}

	public static void printMissingDevices() {
		if (hasMissingDevices()) {
			System.err.println("Missing CAN Devices!");
		} else {
			System.out.println("No missing CAN devices.");
		}

		System.out.println("GrouchLib init'd CAN Devices: ");
		for (var device : getDevices()) {
			System.out.println(device.toString());
		}
	}

	@Override
	public String toString() {
		return String.format("Type: %12s. ID: %2d. On Bus: %5b.", _deviceName, _id, _onBus);
	}
}
