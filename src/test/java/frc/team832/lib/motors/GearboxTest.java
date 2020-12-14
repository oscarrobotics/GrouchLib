package frc.team832.lib.motors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GearboxTest {
	private static Gearbox gearbox = null;

	@BeforeAll
	public static void init() {
		gearbox = new Gearbox(11.23f, 7.0f, 3.0f);
	}

	@Test
	public void getReductionTest() {
		boolean insideIndexException = false;
		boolean outsideIndexException = false;

		float expectedReduction = 11.23f;
		float expectedReduction1 = 7.0f;

		try {
			gearbox.getReduction(0);
		} catch (Exception ex) {
			insideIndexException = true;
		}

		try {
			gearbox.getReduction(3);
		} catch (Exception ex){
			outsideIndexException = true;
		}

		double actualReduction = gearbox.getReduction(0);
		double actualReduction1 = gearbox.getReduction(1);

		assertFalse(insideIndexException, "GetReduction failed on inside index!");
		assertTrue(outsideIndexException, "GetReduction failed on outside index!");

		assertEquals(expectedReduction, actualReduction, 0, "GetReduction failed to return the appropriate value for a certain index");
		assertEquals(expectedReduction1, actualReduction1, 0, "GetReduction failed to return the appropriate value for a certain index");
	}

	@Test
	public void getTotalReductionTest() {
		double totalReduction = 235.83;

		assertEquals(totalReduction, gearbox.getTotalReduction(), 0.001, "GetTotalReduction failed to output correctly");
	}
}
