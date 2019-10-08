//package frc.team832.GrouchLib.motors;
//
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//public class GearboxTest {
//	Gearbox gearbox = null;
//	@Before
//	public void init(){
//		gearbox = new Gearbox((long) 11.23, 7, 3);
//	}
//
//	@Test
//	public void getReductionTest(){
//		boolean insideIndexException = false;
//		boolean outsideIndexException = false;
//
//		double expectedReduction = 11.23;
//		double expectedReduction1 = 7;
//
//		try{
//			gearbox.getReduction(0);
//		} catch (Exception ex) {
//			insideIndexException = true;
//		}
//
//		try{
//			gearbox.getReduction(3);
//		} catch (Exception ex){
//			outsideIndexException = true;
//		}
//
//		double actualReduction = gearbox.getReduction(0);
//		double actualReduction1 = gearbox.getReduction(1);
//
//		assertFalse("GetReduction failed on inside index!", insideIndexException);
//		assertTrue("GetReduction failed on outside index!", outsideIndexException);
//
//		assertEquals("GetReduction failed to return the appropriate value for a certain index", expectedReduction, actualReduction, 0);
//		assertEquals("GetReduction failed to return the appropriate value for a certain index", expectedReduction1, actualReduction1, 0);
//	}
//
//	@Test
//	public void getTotalReductionTest(){
//		double totalReduction = 235.83;
//
//		assertEquals("GetTotalReduction failed to output correctly", totalReduction, gearbox.getTotalReduction(), 0.001);
//	}
//
//}
