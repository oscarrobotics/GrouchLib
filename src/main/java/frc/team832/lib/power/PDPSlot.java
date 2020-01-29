package frc.team832.lib.power;

import edu.wpi.first.wpilibj.MedianFilter;
import frc.team832.lib.control.PDP;
import frc.team832.lib.motors.Motor;

import java.util.function.DoubleSupplier;

@SuppressWarnings("unused")
public class PDPSlot {

    public enum PDPBreaker {
        k20(20),
        k30(30),
        k40(40);

        public final int ratedCurrent;

        PDPBreaker(int ratedCurrent) {
            this.ratedCurrent = ratedCurrent;
        }
    }

    private final PDP pdp;
    final PDPPortNumber pdpPort;
    final PDPBreaker breaker;

    PDPSlot(PDP pdp, PDPPortNumber portNumber, PDPBreaker breaker) {
        this.pdp = pdp;
        this.pdpPort = portNumber;
        this.breaker = breaker;
    }

    int getPDPPortNumber() {
        return pdpPort.ordinal();
    }

    public double getCurrentUsage() {
        return pdp.getChannelCurrent(pdpPort.ordinal());
    }

    public double percentOfBreakerUsage() {
        return breaker.ratedCurrent / getCurrentUsage();
    }

    public static class StallStatus {
        boolean isStalled;
        long stalledForMillis;
    }

    public class StallDetector implements Runnable {
        private int stallCurrent;
        private int minStallMillis;

        private long stallMillis;
        private long lastRunMillis;

        private final MedianFilter currentFilter = new MedianFilter(40); // enough to keep 1 second of data when called every 25ms

        private StallStatus stallStatus = new StallStatus();

        public StallDetector(int stallCurrent, int minStallMillis) {
            this.stallCurrent = stallCurrent;
            this.minStallMillis = minStallMillis;
        }

        public StallStatus getStallStatus() {
            return stallStatus;
        }

        public void setStallCurrent(int stallCurrent) {
            this.stallCurrent = stallCurrent;
        }

        public void setMinStallMillis(int minStallMillis) {
            this.minStallMillis = minStallMillis;
        }

        @Override
        public void run() {
            double currentCurrent = currentFilter.calculate(getCurrentUsage());
            long nowMillis = System.currentTimeMillis();
            long elapsed = nowMillis - lastRunMillis;

            if (currentCurrent >= stallCurrent) {
                stallMillis += elapsed;
            } else {
                stallMillis -= elapsed;
            }

            lastRunMillis = System.currentTimeMillis();

            stallStatus.isStalled = stallMillis >= minStallMillis;
            stallStatus.stalledForMillis = stallMillis;
        }
    }

    public class PredictiveStallDetector implements Runnable {
        // constructor properties
        private int stallCurrent;
        private int minStallMillis;
        private final DoubleSupplier motorSpeedSupplier;
        private final DoubleSupplier motorVoltageSupplier;
        private final Motor motor;

        // run properties
        private long stallMillis;
        private long lastRunMillis;

        private final MedianFilter currentFilter = new MedianFilter(40); // enough to keep 1 second of data when called every 25ms

        private StallStatus stallStatus = new StallStatus();

        public PredictiveStallDetector(int stallCurrent, int minStallMillis, DoubleSupplier motorSpeedSupplier, DoubleSupplier motorVoltageSupplier, Motor motor) {
            this.stallCurrent = stallCurrent;
            this.minStallMillis = minStallMillis;
            this.motorSpeedSupplier = motorSpeedSupplier;
            this.motorVoltageSupplier = motorVoltageSupplier;
            this.motor = motor;
        }

        public StallStatus getStallStatus() {
            return stallStatus;
        }

        public void setStallCurrent(int stallCurrent) {
            this.stallCurrent = stallCurrent;
        }

        public void setMinStallMillis(int minStallMillis) {
            this.minStallMillis = minStallMillis;
        }

        @Override
        public void run() {
            long nowMillis = System.currentTimeMillis();
            long elapsed = nowMillis - lastRunMillis;

            double motorSpeed = motorSpeedSupplier.getAsDouble();
            double motorVoltage = motorVoltageSupplier.getAsDouble();

            double currentCurrent = motor.getPredictiveCurent(motorVoltage, motorSpeed);

            currentCurrent = currentFilter.calculate(currentCurrent);

            if (currentCurrent >= stallCurrent) {
                stallMillis += elapsed;
            } else {
                stallMillis -= elapsed;
            }

            lastRunMillis = System.currentTimeMillis();

            stallStatus.isStalled = stallMillis >= minStallMillis;
            stallStatus.stalledForMillis = stallMillis;
        }
    }
}
