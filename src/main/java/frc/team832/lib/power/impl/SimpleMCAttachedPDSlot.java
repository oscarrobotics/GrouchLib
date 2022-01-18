package frc.team832.lib.power.impl;

import java.util.function.DoubleSupplier;

import frc.team832.lib.motorcontrol.SimpleMC;
import frc.team832.lib.power.GrouchPD;
import frc.team832.lib.power.PDSlot;

public class SimpleMCAttachedPDSlot extends PDSlot {
	private final SimpleMC<?> motorController;
	private final DoubleSupplier motorRPMSupplier;

	public SimpleMCAttachedPDSlot(GrouchPD pdp,
			int portNumber, Breaker breaker, 
			SimpleMC<?> motorController, DoubleSupplier motorRPMSupplier) {
		super(pdp, portNumber, breaker);
		this.motorController = motorController;
		this.motorRPMSupplier = motorRPMSupplier;
	}

	@Override
	public double getCurrentUsage() {
		return motorController.getMotor().getPredictiveCurrent(
			motorController.getOutputVoltage(),
			motorRPMSupplier.getAsDouble()
		);
	}
}
