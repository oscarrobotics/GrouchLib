package frc.team832.lib.power.impl;

import frc.team832.lib.control.PDP;
import frc.team832.lib.motorcontrol2.SimpleMC;
import frc.team832.lib.power.PDPBreaker;
import frc.team832.lib.power.PDPPortNumber;
import frc.team832.lib.power.PDPSlot;

import java.util.function.DoubleSupplier;

public class SimpleMCAttachedPDPSlot extends PDPSlot {
    private final SimpleMC motorController;
    private final DoubleSupplier motorRPMSupplier;

    public SimpleMCAttachedPDPSlot(PDP pdp, PDPPortNumber portNumber, PDPBreaker breaker, SimpleMC motorController,
                                   DoubleSupplier motorRPMSupplier) {
        super(pdp, portNumber, breaker);
        this.motorController = motorController;
        this.motorRPMSupplier = motorRPMSupplier;
    }

    @Override
    public double getCurrentUsage() {
        return motorController.getMotor().getPredictiveCurent(motorController.getOutputVoltage(), motorRPMSupplier.getAsDouble());
    }
}
