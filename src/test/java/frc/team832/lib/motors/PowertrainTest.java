package frc.team832.lib.motors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PowertrainTest {
	private static final float GEARBOX_REDUCTION = 11.23f;
	private static Gearbox gearbox = Gearbox.fromTotalReduction(GEARBOX_REDUCTION);
	private static Powertrain neoPowertrain = new Powertrain(gearbox, Motor.kNEO);
	
	private static Powertrain dualNeoPowertrain = new Powertrain(gearbox, Motor.kNEO, 2);

	@Test
	public void outputSpeedTest() {
		double expected = Motor.kNEO.freeSpeedRPM / gearbox.totalReduction;
		double actual = neoPowertrain.getOutputFreeSpeed();

		System.out.printf("Single getOutputSpeed Test - Expected: %.2f, Actual: %.2f\n", expected, actual);
		assertEquals( expected, actual, 0.001f, "Single getOutputSpeed failed");

		double dualExpected = Motor.kNEO.freeSpeedRPM / gearbox.totalReduction;
		double dualActual = dualNeoPowertrain.getOutputFreeSpeed();

		System.out.printf("Dual getOutputSpeed Test - Expected: %.2f, Actual: %.2f\n", dualExpected, dualActual);
		assertEquals(dualExpected, dualActual, 0.001f, "Dual getOutputSpeed failed");
	}

	@Test
	public void freeCurrentTest(){
		double expected = Motor.kNEO.freeCurrentAmps * neoPowertrain.motorCount;
		double actual = neoPowertrain.getFreeCurrent();

		System.out.printf("Single getFreeCurrent Test - Expected: %.2f, Actual: %.2f\n", expected, actual);
		assertEquals(expected, actual, 0.001f, "Single getFreeCurrent failed");

		double dualExpected = Motor.kNEO.freeCurrentAmps * dualNeoPowertrain.motorCount;
		double dualActual = dualNeoPowertrain.getFreeCurrent();

		System.out.printf("Dual getFreeCurrent Test - Expected: %.2f, Actual: %.2f\n", dualExpected, dualActual);
		assertEquals(dualExpected, dualActual, 0.001f, "Dual getFreeCurrent failed!");
	}

	@Test
	public void stallCurrentTest(){
		double expected = Motor.kNEO.stallCurrentAmps * neoPowertrain.motorCount;
		double actual = neoPowertrain.getStallCurrent();

		assertEquals(expected, actual, 0.001f, "getStallCurrent failed");

		double dualExpected = Motor.kNEO.stallCurrentAmps * dualNeoPowertrain.motorCount;
		double dualActual = dualNeoPowertrain.getStallCurrent();

		System.out.printf("Dual getStallCurrent Test - Expected: %.2f, Actual: %.2f\n", dualExpected, dualActual);
		assertEquals(dualExpected, dualActual, 0.001f, "getStallCurrent failed");
	}

	@Test
	public void stallTorqueTest(){
		double expected = (neoPowertrain.motorCount * Motor.kNEO.stallTorqueNewtonMeters) * gearbox.totalReduction;
		double actual = neoPowertrain.getOutputStallTorque();

		assertEquals(expected, actual, 0.001f, "getStallTorque failed");
	}
}
