package frc.team832.lib.power.management;

import frc.team832.lib.power.management.ConstrainedPowerSolver;
import frc.team832.lib.power.management.PowerManagedDevice;

public class PowerManagedSubsystem extends PowerManagedDevice {
    private final ConstrainedPowerSolver subsystemPowerManager = new ConstrainedPowerSolver();

    public PowerManagedSubsystem(double weight) {
        super(weight);
    }

    public void addDevices(PowerManagedDevice... devices) {
//        subsystemPowerManager.addDevices(devices);
    }

    @Override
    public void applyCurrentLimit() {
        subsystemPowerManager.solve((int) getConstrainedCurrentAmps());
    }
}
