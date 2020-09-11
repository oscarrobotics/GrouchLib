package frc.team832.lib.power.management;


public abstract class PowerManager {

    public PowerManager(PowerManagedDevice... devices) {
    }

    abstract void addDevice(PowerManagedDevice device);
    abstract void run();
}
