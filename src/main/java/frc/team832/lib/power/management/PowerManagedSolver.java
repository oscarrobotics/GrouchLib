package frc.team832.lib.power.management;

import frc.team832.lib.motorcontrol2.PowerManagedMC;

public class PowerManagedSolver extends PowerManagedDevice {

    private final ConstrainedPowerSolver solver = new ConstrainedPowerSolver();

    public PowerManagedSolver(PowerManagedMC... devices) {
        solver.addDevices(devices);
    }

    public PowerManagedSolver(PowerManagedDevice... devices) {
        solver.addDevices(devices);
    }

    @Override
    public int getRequestedCurrentAmps() {
        return solver.getDevices().stream().mapToInt(PowerManagedDevice::getRequestedCurrentAmps).sum();
    }

    @Override
    public void applyCurrentLimit() {
        solver.solve(getConstrainedCurrentAmps());
    }
}
