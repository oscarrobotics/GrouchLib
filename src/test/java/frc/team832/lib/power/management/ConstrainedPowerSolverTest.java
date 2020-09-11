package frc.team832.lib.power.management;

import frc.team832.lib.motorcontrol2.vendor.CANSparkMax;
import frc.team832.lib.motors.Motor;
import frc.team832.lib.util.NumberListUtils;
import frc.team832.lib.util.math.OscarMath;
import org.junit.jupiter.api.*;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class ConstrainedPowerSolverTest {

    private CANSparkMax testSparkMax0;
    private CANSparkMax testSparkMax1, testSparkMax2;
    private ConstrainedPowerSolver powerSolver;

    @BeforeEach
    public void init() {
        testSparkMax0 = new CANSparkMax(0, Motor.kNEO);
        testSparkMax1 = new CANSparkMax(1, Motor.kNEO);
        testSparkMax2 = new CANSparkMax(2, Motor.kNEO);
        powerSolver = new ConstrainedPowerSolver();
        // solve once before timing to avoid first-run delays
        powerSolver.solve(20);
    }

    @Test
    public void constrainSingleDevice() {
        powerSolver.addDevices(testSparkMax0);
        testSparkMax0.getPowerManagement().setRequestedCurrentAmps(30);

        long solveStartNanos = System.nanoTime();
        powerSolver.solve(20);
        double millis = (System.nanoTime() - solveStartNanos) * 1e-6;

        assertEquals(testSparkMax0.getPowerManagement().getConstrainedCurrentAmps(), 20);
        System.out.printf("constrainSingleDevice() - solved in %.2fms\n", millis);
    }

    @Test
    public void underconstrainSingleDevice() {
        // add device to solver
        powerSolver.addDevices(testSparkMax0);

        int deviceRequest = 30;

        // set device request to 30 amps
        testSparkMax0.getPowerManagement().setRequestedCurrentAmps(deviceRequest);

        // set limitation to double deviceRequest
        long solveStartNanos = System.nanoTime();
        powerSolver.solve(deviceRequest * 2);
        double millis = (System.nanoTime() - solveStartNanos) * 1e-6;

        assertEquals(testSparkMax0.getPowerManagement().getConstrainedCurrentAmps(), deviceRequest);
        System.out.printf("underconstrainSingleDevice() - solved in %.2fms\n", millis);
    }

    @Test
    public void constrainMultipleDevices_SameWeightSameRequest() {
        // add devices to solver
        powerSolver.addDevices(testSparkMax0, testSparkMax1, testSparkMax2);

        // set all device weights to the same weight
        testSparkMax0.getPowerManagement().setWeight(0.001);
        testSparkMax1.getPowerManagement().setWeight(0.001);
        testSparkMax2.getPowerManagement().setWeight(0.001);

        int deviceRequest = 20;
        int maxCurrent = (deviceRequest * 3) / 2;
        int expectedConstrained = deviceRequest / 2;

        // set device requests to varying currents
        testSparkMax0.getPowerManagement().setRequestedCurrentAmps(deviceRequest);
        testSparkMax1.getPowerManagement().setRequestedCurrentAmps(deviceRequest);
        testSparkMax2.getPowerManagement().setRequestedCurrentAmps(deviceRequest);

        // solve for power
        long solveStartNanos = System.nanoTime();
        powerSolver.solve(maxCurrent);
        double millis = (System.nanoTime() - solveStartNanos) * 1e-6;

        double device0Constrained = testSparkMax0.getPowerManagement().getConstrainedCurrentAmps();
        double device1Constrained = testSparkMax1.getPowerManagement().getConstrainedCurrentAmps();
        double device2Constrained = testSparkMax2.getPowerManagement().getConstrainedCurrentAmps();

        boolean device0OK = expectedConstrained == device0Constrained;
        boolean device1OK = expectedConstrained == device1Constrained;
        boolean device2OK = expectedConstrained == device2Constrained;

        assertTrue(device0OK);
        assertTrue(device1OK);
        assertTrue(device2OK);

        System.out.printf("constrainMultipleDevices_SameWeightSameRequest() - solved in %.2fms\n", millis);
    }

    @Test
    public void constrainMultipleDevices_SameWeightRandomRequest_100Iterations() {
        // add devices to solver
        powerSolver.addDevices(testSparkMax0, testSparkMax1, testSparkMax2);

        // set all device weights to the same weight
        testSparkMax0.getPowerManagement().setWeight(0.001);
        testSparkMax1.getPowerManagement().setWeight(0.001);
        testSparkMax2.getPowerManagement().setWeight(0.001);

        int deviceRequest = 20;
        int maxCurrent = (deviceRequest * 3) / 2;
//        int expectedConstrained = deviceRequest / 2;

        List<Double> times = new ArrayList<>();

        long startTimeNanos, endTimeNanos;
        double elapsedTimeMillis;
        for (int i = 0; i < 100 ; i++) {
            // set device requests to varying currents
            testSparkMax0.getPowerManagement().setRequestedCurrentAmps(new Random().nextInt(30));
            testSparkMax1.getPowerManagement().setRequestedCurrentAmps(new Random().nextInt(30));
            testSparkMax2.getPowerManagement().setRequestedCurrentAmps(new Random().nextInt(30));

            startTimeNanos = System.nanoTime();
            powerSolver.solve(maxCurrent);
            endTimeNanos = System.nanoTime();
            elapsedTimeMillis = OscarMath.round((endTimeNanos - startTimeNanos) * 1e-6, 4);
            times.add(elapsedTimeMillis);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        double minMillis = Collections.min(times);
        double meanMillis = NumberListUtils.mean(times);
        double maxMillis = Collections.max(times);

        // show chart (skipped if java.awt.headless is true in build.gradle)
        try {
            var chartBuilder = new XYChartBuilder();
            chartBuilder.title = "Run times";
            chartBuilder.xAxisTitle("Iteration");
            chartBuilder.yAxisTitle("Millis");
            var chart = chartBuilder.build();
            List<Integer> xVals = IntStream.range(0, 100).boxed().collect(Collectors.toList());
            chart.addSeries("Time (ms)", xVals, times);

            new SwingWrapper<>(chart).displayChart();
            try { Thread.sleep(1000000000); } catch (InterruptedException e) { /* ignored */ }
        } catch (java.awt.HeadlessException ex) {
            System.out.println("constrainMultipleDevices_SameWeightVaryingRequest_100Iterations() - skipping chart display in headless mode");
        }

        System.out.printf("constrainMultipleDevices_SameWeightVaryingRequest_100Iterations() - solved. min: %.2fms, mean %.2fms, max: %.2fms\n", minMillis, meanMillis, maxMillis);
    }

    @Test
    public void nestedSolver_SubdeviceRequestTotal() {
        PowerManagedSolver nestedSolver = new PowerManagedSolver(testSparkMax0, testSparkMax1, testSparkMax2);

        int[] requests = {new Random().nextInt(30), new Random().nextInt(30), new Random().nextInt(30)};
        int totalRequest = requests[0] + requests[1] + requests[2];

        testSparkMax0.getPowerManagement().setRequestedCurrentAmps(requests[0]);
        testSparkMax1.getPowerManagement().setRequestedCurrentAmps(requests[1]);
        testSparkMax2.getPowerManagement().setRequestedCurrentAmps(requests[2]);

        // ensure nested solver requests correct currents
        assertEquals(nestedSolver.getRequestedCurrentAmps(), totalRequest);
    }

    @Test
    public void nestedSolver_SubdeviceWeight() {
        PowerManagedSolver nestedSolver = new PowerManagedSolver(testSparkMax0, testSparkMax1, testSparkMax2);
        testSparkMax0.getPowerManagement().setWeight(0.2);
        testSparkMax1.getPowerManagement().setWeight(0.3);
        testSparkMax2.getPowerManagement().setWeight(0.4);
        nestedSolver.setWeight(0.1);

        assertEquals(nestedSolver.getWeight(), 0.1);
        assertEquals(testSparkMax0.getPowerManagement().getWeight(), 0.2);
        assertEquals(testSparkMax1.getPowerManagement().getWeight(), 0.3);
        assertEquals(testSparkMax2.getPowerManagement().getWeight(), 0.4);
    }

    @Test
    public void constrainNestedSolver_SubdevicesSameWeight() {
        PowerManagedSolver nestedSolver = new PowerManagedSolver(testSparkMax0, testSparkMax1, testSparkMax2);
        testSparkMax0.getPowerManagement().setWeight(0.2);
        testSparkMax1.getPowerManagement().setWeight(0.2);
        testSparkMax2.getPowerManagement().setWeight(0.2);
        testSparkMax0.getPowerManagement().setRequestedCurrentAmps(10);
        testSparkMax1.getPowerManagement().setRequestedCurrentAmps(10);
        testSparkMax2.getPowerManagement().setRequestedCurrentAmps(10);

        nestedSolver.setWeight(0.1);

        int deviceCount = 3;
        int deviceRequest = 10;
        int maxCurrent = 25;
        double rawConstrained = 25.0 / deviceCount;
        double trueConstrained = OscarMath.round(rawConstrained, 0);

        powerSolver.addDevices(nestedSolver);
        powerSolver.solve(maxCurrent);


        double[] constrainedCurrents = {
                testSparkMax0.getPowerManagement().getConstrainedCurrentAmps(),
                testSparkMax1.getPowerManagement().getConstrainedCurrentAmps(),
                testSparkMax2.getPowerManagement().getConstrainedCurrentAmps()
        };

        assertArrayEquals(constrainedCurrents, new double[]{trueConstrained, trueConstrained, trueConstrained});
    }

    @Test
    public void constrainNestedSolverWithExternalDevice() {
        testSparkMax0.getPowerManagement().setRequestedCurrentAmps(30);
        testSparkMax1.getPowerManagement().setRequestedCurrentAmps(30);
        PowerManagedSolver nestedSolver = new PowerManagedSolver(testSparkMax0, testSparkMax1);
        nestedSolver.setWeight(0.3);

        testSparkMax2.getPowerManagement().setRequestedCurrentAmps(30);

    }

    @AfterEach
    public void cleanup() {
        powerSolver = null;
        testSparkMax2 = null;
        testSparkMax1 = null;
        testSparkMax0 = null;
    }
}
