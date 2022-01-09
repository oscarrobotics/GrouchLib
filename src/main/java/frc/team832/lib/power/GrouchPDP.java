package frc.team832.lib.power;

import frc.team832.lib.control.PDP;
import frc.team832.lib.motorcontrol.SimpleMC;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.power.impl.SimpleMCAttachedPDPSlot;
import frc.team832.lib.power.impl.SmartMCAttachedPDPSlot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;

public class GrouchPDP {

	private final PDP pdp;
	private final List<PDPSlot> ports = new ArrayList<>();

	public GrouchPDP(int pdpCANID) {
		pdp = new PDP(pdpCANID);
	}

	private boolean hasAssignedPort(PDPPortNumber port) {
		return ports.stream().anyMatch(p -> port == p.pdpPort);
	}

	public PDPSlot addDevice(PDPPortNumber portNumber) {
		return addDevice(portNumber, portNumber.maxBreaker);
	}

	public PDPSlot addDevice(PDPPortNumber portNumber, PDPBreaker breaker) {
		assert !hasAssignedPort(portNumber) : "PDP Port already in use!";
		assert breaker == portNumber.maxBreaker || breaker == portNumber.minBreaker : "Invalid breaker for given port!";

		PDPSlot slot = new PDPSlot(pdp, portNumber, breaker);

		ports.add(slot);
		return slot;
	}

	public PDPSlot addDevice(PDPPortNumber portNumber, SimpleMC<?> motorController,
							 DoubleSupplier motorRPMSupplier) {
		return addDevice(portNumber, portNumber.maxBreaker, motorController, motorRPMSupplier);
	}

	public SimpleMCAttachedPDPSlot addDevice(
			PDPPortNumber portNumber, PDPBreaker breaker,
			SimpleMC<?> motorController, DoubleSupplier motorRPMSupplier) {
		assert !hasAssignedPort(portNumber) : "PDP Port already in use!";
		assert breaker == portNumber.maxBreaker || breaker == portNumber.minBreaker : "Invalid breaker for given port!";

		SimpleMCAttachedPDPSlot slot = new SimpleMCAttachedPDPSlot(pdp, portNumber, breaker, motorController, motorRPMSupplier);

		ports.add(slot);
		return slot;
	}

	public SmartMCAttachedPDPSlot addDevice(PDPPortNumber portNumber, SmartMC<?> motorController) {
		return addDevice(portNumber, portNumber.maxBreaker, motorController);
	}

	public SmartMCAttachedPDPSlot addDevice(PDPPortNumber portNumber, PDPBreaker breaker, SmartMC<?> motorController) {
		assert !hasAssignedPort(portNumber) : "PDP Port already in use!";
		assert breaker == portNumber.maxBreaker || breaker == portNumber.minBreaker : "Invalid breaker for given port!";

		SmartMCAttachedPDPSlot slot = new SmartMCAttachedPDPSlot(pdp, portNumber, breaker, motorController);

		ports.add(slot);
		return slot;
	}

	public PDP getBasePDP() { return pdp; }
}
