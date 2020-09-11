package frc.team832.lib.motors;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PowertrainTest {
	private static final float GEARBOX_REDUCTION = 11.23f;
	private static final Gearbox gearbox = new Gearbox(GEARBOX_REDUCTION);
	private static final Powertrain neoPowertrain = new Powertrain(gearbox, Motor.kNEO);
	private static final Powertrain dualNeoPowertrain = new Powertrain(gearbox, Motor.kNEO, 2);


	@Test
	public void outputSpeedTest() {
		double expected = Motor.kNEO.freeSpeed / GEARBOX_REDUCTION;
		double actual = neoPowertrain.getOutputSpeed();

        System.out.printf("Single getOutputSpeed Test - Expected: %.2f, Actual: %.2f\n", expected, actual);
		assertEquals(expected, actual, 0, "Single getOutputSpeed failed");

		double dualExpected = Motor.kNEO.freeSpeed / GEARBOX_REDUCTION;
		double dualActual = dualNeoPowertrain.getOutputSpeed();

        System.out.printf("Dual getOutputSpeed Test - Expected: %.2f, Actual: %.2f\n", dualExpected, dualActual);
        assertEquals(dualExpected, dualActual, 0, "Dual getOutputSpeed failed");
	}

	@Test
	public void freeCurrentTest(){
		double expected = Motor.kNEO.freeCurrent * neoPowertrain.getMotorCount();
		double actual = neoPowertrain.getFreeCurrent();

        System.out.printf("Single getFreeCurrent Test - Expected: %.2f, Actual: %.2f\n", expected, actual);
		assertEquals(expected, actual, 0, "Single getFreeCurrent failed");

        double dualExpected = Motor.kNEO.freeCurrent * dualNeoPowertrain.getMotorCount();
        double dualActual = dualNeoPowertrain.getFreeCurrent();

        System.out.printf("Dual getFreeCurrent Test - Expected: %.2f, Actual: %.2f\n", dualExpected, dualActual);
        assertEquals(dualExpected, dualActual, 0, "Dual getFreeCurrent failed!");
	}

	@Test
	public void stallCurrentTest(){
		double expected = Motor.kNEO.stallCurrent * neoPowertrain.getMotorCount();
		double actual = neoPowertrain.getStallCurrent();

		assertEquals(expected, actual, 0, "getStallCurrent failed");

        double dualExpected = Motor.kNEO.stallCurrent * dualNeoPowertrain.getMotorCount();
        double dualActual = dualNeoPowertrain.getStallCurrent();

        System.out.printf("Dual getStallCurrent Test - Expected: %.2f, Actual: %.2f\n", dualExpected, dualActual);
        assertEquals(dualExpected, dualActual, 0, "getStallCurrent failed");
	}

	@Test
	public void stallTorqueTest(){
		double expected = (neoPowertrain.getMotorCount() * Motor.kNEO.stallTorque) * GEARBOX_REDUCTION;
		double actual = neoPowertrain.getStallTorque();

		assertEquals(expected, actual, 0, "getStallTorque failed");
	}
}
