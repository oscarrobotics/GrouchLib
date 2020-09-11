package frc.team832.lib.structure.subsys;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.lib.driverstation.dashboard.DashboardTab;
import frc.team832.lib.power.management.PowerManagedSubsystem;
import frc.team832.lib.power.management.PowerManagedDevice;

public abstract class OscarSubsystem extends SubsystemBase implements AutoCloseable {

    private final PowerManagedDevice powerDevice = new PowerManagedSubsystem(0);
    protected final DashboardTab dashboardTab;

    public OscarSubsystem(String subsystemName) {
        dashboardTab = new DashboardTab(subsystemName);
    }

    protected void setPowerPriority(double priority) {
        powerDevice.setWeight(priority);
    }

    @Override
    public abstract void close();
}
