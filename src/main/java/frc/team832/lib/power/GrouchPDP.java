package frc.team832.lib.power;

import frc.team832.lib.control.PDP;

import java.util.ArrayList;
import java.util.List;

public class GrouchPDP {

    private final PDP pdp;
    private final List<PDPSlot> ports = new ArrayList<>();

    public GrouchPDP(int pdpCANID) {
        pdp = new PDP(pdpCANID);
    }

    public GrouchPDP(PDP pdp) {
        this.pdp = pdp;
    }

    private boolean hasAssignedPort(PDPPortNumber port) {
        return ports.stream().anyMatch(p -> port == p.pdpPort);
    }

    public void addDevice(PDPPortNumber portNumber) {
        addDevice(portNumber, portNumber.maxBreaker);
    }

    public void addDevice(PDPPortNumber portNumber, PDPSlot.PDPBreaker breaker) {
        assert !hasAssignedPort(portNumber) : "PDP Port already in use!";
        assert breaker == portNumber.maxBreaker || breaker == portNumber.minBreaker : "Invalid breaker for given port!";

        PDPSlot newSlot = new PDPSlot(pdp, portNumber, breaker);
    }
}
