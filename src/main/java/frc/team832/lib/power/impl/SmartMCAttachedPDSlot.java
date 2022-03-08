package frc.team832.lib.power.impl;

import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.power.GrouchPD;
import frc.team832.lib.power.PDSlot;

public class SmartMCAttachedPDSlot extends PDSlot {
	private final SmartMC<?, ?> motorController;

	public SmartMCAttachedPDSlot(GrouchPD pd, int portNumber, Breaker breaker, SmartMC<?, ?> motorController) {
		super(pd, portNumber, breaker);
		this.motorController = motorController;
	}

	@Override
	public double getCurrentUsage() {
		return motorController.getOutputCurrent();
	}
}
