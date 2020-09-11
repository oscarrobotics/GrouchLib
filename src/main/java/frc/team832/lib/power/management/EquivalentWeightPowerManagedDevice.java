package frc.team832.lib.power.management;

import java.util.List;

public class EquivalentWeightPowerManagedDevice extends PowerManagedDevice {

    private final List<PowerManagedDevice> devices;

    public EquivalentWeightPowerManagedDevice(double weight, List<PowerManagedDevice> devices) {
        this.devices = devices;
        setWeight(weight);

        int requestSum = 0;
        for (var dev : devices) {
            requestSum += dev.getRequestedCurrentAmps();
        }
        setRequestedCurrentAmps(requestSum);
    }

    @Override
    protected void applyConstrainedCurrentAmps(double constrainedCurrentAmps) {
        double perDeviceCurrent = constrainedCurrentAmps / devices.size();
        for (var dev : devices) {
            dev.applyConstrainedCurrentAmps(perDeviceCurrent);
        }
    }

    @Override
    public void applyCurrentLimit() {
        for (var dev : devices) {
            dev.applyCurrentLimit();
        }
    }
}
