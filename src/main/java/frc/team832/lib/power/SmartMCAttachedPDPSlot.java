package frc.team832.lib.power;

import frc.team832.lib.control.PDP;
import frc.team832.lib.motorcontrol2.SmartMC;
import frc.team832.lib.motors.Motor;

public class SmartMCAttachedPDPSlot extends PDPSlot {
    private final SmartMC motorController;
    private final Motor motor;

    public SmartMCAttachedPDPSlot(PDP pdp, PDPPortNumber portNumber, PDPBreaker breaker, SmartMC motorController, Motor motor) {
        super(pdp, portNumber, breaker);
        this.motorController = motorController;
        this.motor = motor;
    }

    @Override
    public double getCurrentUsage() {
        return motorController.getOutputCurrent();
    }
}
