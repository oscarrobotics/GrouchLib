package frc.team832.lib.power.impl;

import frc.team832.lib.control.PDP;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.power.PDPBreaker;
import frc.team832.lib.power.PDPPortNumber;
import frc.team832.lib.power.PDPSlot;

public class SmartMCAttachedPDPSlot extends PDPSlot {
	private final SmartMC<?> motorController;

	public SmartMCAttachedPDPSlot(PDP pdp, PDPPortNumber portNumber, PDPBreaker breaker, SmartMC<?> motorController) {
		super(pdp, portNumber, breaker);
		this.motorController = motorController;
	}

	@Override
	public double getCurrentUsage() {
		return motorController.getOutputCurrent();
	}
}
