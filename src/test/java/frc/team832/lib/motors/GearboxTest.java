package frc.team832.lib.motors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GearboxTest {
	private static final Gearbox m_drivetrainGearbox = Gearbox.fromStages(11.0 / 60.0, 16.0 / 32.0);

	@Test
	public void getReductionTest() {
		boolean insideIndexException = false;
		boolean outsideIndexException = false;

		// numbers greater than 1 are reductions.
		double expectedReduction = 1 / (11.0 / 60.0);
		double expectedReduction1 = 1 / (16.0 / 32.0);

		try {
			m_drivetrainGearbox.getReduction(0);
		} catch (Exception ex) {
			insideIndexException = true;
		}

		try {
			m_drivetrainGearbox.getReduction(3);
		} catch (Exception ex){
			outsideIndexException = true;
		}

		double actualReduction = m_drivetrainGearbox.getReduction(0);
		double actualReduction1 = m_drivetrainGearbox.getReduction(1);

		assertFalse(insideIndexException, "GetReduction failed on inside index!");
		assertTrue(outsideIndexException, "GetReduction failed on outside index!");

		assertEquals(expectedReduction, actualReduction, 0.001f, "GetReduction failed to return the appropriate value for a certain index");
		assertEquals(expectedReduction1, actualReduction1, 0.001f, "GetReduction failed to return the appropriate value for a certain index");
	}

	@Test
	public void getTotalReductionTest() {
		var gearbox = Gearbox.fromStages(1 / 7.0f, 1 / 3.0f);
		double totalReduction = 21;

		assertEquals(totalReduction, gearbox.totalReduction, 0.001f, "GetTotalReduction failed to output correctly");
	}
}
