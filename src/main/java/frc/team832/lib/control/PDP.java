package frc.team832.lib.control;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import frc.team832.lib.CANDevice;

public class PDP {

	public final PowerDistribution _pdp;

	private final boolean onBus;

	public PDP(int canID) {
		_pdp = new PowerDistribution(canID, ModuleType.kCTRE);

		onBus = _pdp.getVoltage() > 0; // TODO: better way to do this?
		CANDevice.addDevice(canID, onBus, "PDP");
	}

	public double getTotalCurrent() {
		return onBus ? _pdp.getTotalCurrent() : -1;
	}

	public double getTotalPower() {
		return onBus ? _pdp.getTotalPower() : -1;
	}

	public double getChannelCurrent(int channel) {
		return onBus ? _pdp.getCurrent(channel) : -1;
	}

	public double getChannelPower(int channel) { return onBus ? getChannelCurrent(channel) * getVoltage() : -1; }

	public double getTemperature() { return onBus ? _pdp.getTemperature() : -1; }

	public double getVoltage() { return onBus ?_pdp.getVoltage() : -1; }

	public boolean isOnBus () { return onBus; }

}
