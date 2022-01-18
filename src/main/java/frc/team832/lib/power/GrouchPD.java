package frc.team832.lib.power;

import frc.team832.lib.motorcontrol.SimpleMC;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.power.PDSlot.Breaker;
import frc.team832.lib.power.impl.SimpleMCAttachedPDSlot;
import frc.team832.lib.power.impl.SmartMCAttachedPDSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.PowerDistribution;

public class GrouchPD extends PowerDistribution {

	public final ModuleType pdType;
	private final List<PDSlot> ports = new ArrayList<>();

	public GrouchPD(int module, ModuleType moduleType) {
		super(module, moduleType);
		pdType = moduleType;
	}

	private boolean hasAssignedPort(int portNumber) {
		return ports.stream().anyMatch(p -> portNumber == p.getPDPortNumber());
	}

	public PDSlot addDevice(int portNumber, Breaker breaker) {
		assert !hasAssignedPort(portNumber) : "PDP Port already in use!";
		var slot = new PDSlot(this, portNumber, breaker);

		ports.add(slot);
		return slot;
	}

	public SimpleMCAttachedPDSlot addDevice(
			int portNumber, Breaker breaker,
			SimpleMC<?> motorController, DoubleSupplier motorRPMSupplier) {
		assert !hasAssignedPort(portNumber) : "PD Port already in use!";

		var slot = new SimpleMCAttachedPDSlot(this, portNumber, breaker, motorController, motorRPMSupplier);

		ports.add(slot);
		return slot;
	}

	public SmartMCAttachedPDSlot addDevice(int portNumber, Breaker breaker, SmartMC<?> motorController) {
		assert !hasAssignedPort(portNumber) : "PD Port already in use!";
		var slot = new SmartMCAttachedPDSlot(this, portNumber, breaker, motorController);

		ports.add(slot);
		return slot;
	}
}
