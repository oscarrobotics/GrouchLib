package frc.team832.lib.power.management;

import frc.team832.lib.motorcontrol2.PowerManagedMC;
import frc.team832.lib.util.math.OscarMath;
import frc.team832.lib.util.math.cobyla.Calcfc;
import frc.team832.lib.util.math.cobyla.Cobyla;
import frc.team832.lib.util.math.cobyla.CobylaExitStatus;

import java.util.*;
import java.util.stream.Collectors;

public class ConstrainedPowerSolver {
    private final static double kRhoBeg = 0.2;
    private final static double kRhoEnd = 1.0e-10;
    private final static int kIPrint = 0;
    private final static int kMaxFun = 50;

    private final List<PowerManagedDevice> userAddedDeviceList = new ArrayList<>();
    private final List<PowerManagedDevice> internalManagedDeviceList = new ArrayList<>();

    public void addDevices(PowerManagedMC... devices) {
        for (PowerManagedMC device : devices) {
            addDevice(device.getPowerManagement());
        }
    }

    public void addDevices(PowerManagedDevice... devices) {
        for (var dev : devices) {
            addDevice(dev);
        }
    }

    private void addDevice(PowerManagedDevice device) {
        if (!userAddedDeviceList.contains(device)) {
            userAddedDeviceList.add(device);
        }
    }

    public void solve(double maxCurrent) {
        if (userAddedDeviceList.size() == 0) {
            return;
        }

        Map<Double, List<PowerManagedDevice>> devicesByWeight = userAddedDeviceList.stream().collect(Collectors.groupingBy(PowerManagedDevice::getWeight));
        double minWeight = Collections.min(devicesByWeight.keySet());

        internalManagedDeviceList.clear();
        devicesByWeight.forEach((weight, devGroup) -> {
            if (devGroup.size() > 1) {
                internalManagedDeviceList.add(new EquivalentWeightPowerManagedDevice(weight, devGroup));
            } else {
                internalManagedDeviceList.add(devGroup.get(0));
            }
        });

        // give 0-weight devices a weight 99% of minWeight, unless all devices are 0-weight.
        minWeight = minWeight == 0 ? 0.1 : minWeight - (minWeight * .01);

        for (var device : internalManagedDeviceList) {
            if (device.getWeight() == 0) {
                device.internalWeight = minWeight;
            } else {
                device.internalWeight = device.getWeight();
            }
        }

        int deviceCount = internalManagedDeviceList.size();
        int constraintCount = deviceCount * 2 + 1;
        if (deviceCount == 0) {
            System.err.println("Cannot solve with no devices!");
            return;
        }

        double[] mastx = new double[deviceCount];
        Arrays.fill(mastx, 0.0);

        // exit status is ignored
        var result = Cobyla.FindMinimum(getCalc(maxCurrent, internalManagedDeviceList), deviceCount, constraintCount, mastx, kRhoBeg, kRhoEnd, kIPrint, kMaxFun);

        if (result == CobylaExitStatus.MaxIterationsReached) {
            System.out.println("Solving hit max iterations.");
        }

        for (int i = 0; i < deviceCount; i++) {
            PowerManagedDevice dev = internalManagedDeviceList.get(i);
            int requestedCurrentAmps = dev.getRequestedCurrentAmps();
            double constraint = OscarMath.round(mastx[i], 3); // maximum of 3 places to try and keep whole number results possible
            double constrainedCurrentAmps = constraint * requestedCurrentAmps;
            dev.applyConstrainedCurrentAmps(constrainedCurrentAmps);
        }
    }

    private Calcfc getCalc(double maxCurrent, List<PowerManagedDevice> devices) {
        return (n, m, x, con) -> {
            double retVal = 0.0;
            int count = devices.size();
            con[0] = maxCurrent;

            for (int i = 0; i < count; i++) {
                con[0] -= (devices.get(i).getRequestedCurrentAmps() * x[i]);
            }

            for (int i = 0; i < count; i++) {
                con[i + 1] = (1 - x[i]);
            }

            for (int i = 0; i < count; i++) {
                con[i + 1 + count] = (x[i] - 0);
            }

            for (int i = 0; i < count; i++) {
                double deviceWeight = devices.get(i).internalWeight;
                retVal += -(deviceWeight) * Math.pow(x[i], 2.0);
            }

            return retVal;
        };
    }

    public List<PowerManagedDevice> getDevices() {
        return userAddedDeviceList;
    }
}
