package frc.team832.lib.motors;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PowertrainTest {
	private Powertrain neoPowertrain = null;
	private Powertrain dualNeoPowertrain = null;
    private static final float GEARBOX_REDUCTION = 11.23f;

	@Before
	public void init() {
		Gearbox gearbox = new Gearbox(GEARBOX_REDUCTION);
		neoPowertrain = new Powertrain(gearbox, Motor.kNEO);
		dualNeoPowertrain = new Powertrain(gearbox, Motor.kNEO, 2);
	}

	@Test
	public void outputSpeedTest() {
		double expected = Motor.kNEO.freeSpeed / GEARBOX_REDUCTION;
		double actual = neoPowertrain.getOutputSpeed();

        System.out.printf("Single getOutputSpeed Test - Expected: %.2f, Actual: %.2f\n", expected, actual);
		assertEquals("Single getOutputSpeed failed", expected, actual, 0);

		double dualExpected = Motor.kNEO.freeSpeed / GEARBOX_REDUCTION;
		double dualActual = dualNeoPowertrain.getOutputSpeed();

        System.out.printf("Dual getOutputSpeed Test - Expected: %.2f, Actual: %.2f\n", dualExpected, dualActual);
        assertEquals("Dual getOutputSpeed failed", dualExpected, dualActual, 0);
	}

	@Test
	public void freeCurrentTest(){
		double expected = Motor.kNEO.freeCurrent * neoPowertrain.getMotorCount();
		double actual = neoPowertrain.getFreeCurrent();

        System.out.printf("Single getFreeCurrent Test - Expected: %.2f, Actual: %.2f\n", expected, actual);
		assertEquals("Single getFreeCurrent failed", expected, actual, 0);

        double dualExpected = Motor.kNEO.freeCurrent * dualNeoPowertrain.getMotorCount();
        double dualActual = dualNeoPowertrain.getFreeCurrent();

        System.out.printf("Dual getFreeCurrent Test - Expected: %.2f, Actual: %.2f\n", dualExpected, dualActual);
        assertEquals("Dual getFreeCurrent failed!", dualExpected, dualActual, 0);
	}

	@Test
	public void stallCurrentTest(){
		double expected = Motor.kNEO.stallCurrent * neoPowertrain.getMotorCount();
		double actual = neoPowertrain.getStallCurrent();

		assertEquals("getStallCurrent failed", expected, actual, 0);

        double dualExpected = Motor.kNEO.stallCurrent * dualNeoPowertrain.getMotorCount();
        double dualActual = dualNeoPowertrain.getStallCurrent();

        System.out.printf("Dual getStallCurrent Test - Expected: %.2f, Actual: %.2f\n", dualExpected, dualActual);
        assertEquals("getStallCurrent failed", dualExpected, dualActual, 0);
	}

	@Test
	public void stallTorqueTest(){
		double expected = (neoPowertrain.getMotorCount() * Motor.kNEO.stallTorque) * GEARBOX_REDUCTION;
		double actual = neoPowertrain.getStallTorque();

		assertEquals("getStallTorque failed", expected, actual, 0);
	}
}
