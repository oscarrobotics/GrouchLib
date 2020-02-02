package frc.team832.lib.power;

import frc.team832.lib.control.PDP;

@SuppressWarnings("unused")
public class PDPSlot {
    private final PDP pdp;
    final PDPPortNumber pdpPort;
    private final PDPBreaker breaker;

    protected PDPSlot(PDP pdp, PDPPortNumber portNumber, PDPBreaker breaker) {
        this.pdp = pdp;
        this.pdpPort = portNumber;
        this.breaker = breaker;
    }


    int getPDPPortNumber() {
        return pdpPort.ordinal();
    }

    public int getBreakerRatedCurrent() {
        return breaker.ratedCurrent;
    }

    public double getCurrentUsage() {
        return pdp.getChannelCurrent(pdpPort.ordinal());
    }

    public double percentOfBreakerUsage() {
        return breaker.ratedCurrent / getCurrentUsage();
    }
}
