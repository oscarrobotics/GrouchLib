//package frc.team832.GrouchLib.motors;
//
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//public class PowertrainTest {
//	Powertrain powertrain = null;
//	@Before
//	public void init(){
//		Gearbox gearbox = new Gearbox((long) 11.23);
//		Motor motor = Motors.NEO;
//		powertrain = new Powertrain(gearbox, motor);
//	}
//
//	@Test
//	public void outputSpeedTest(){
//		double expected = 11.23*5676;
//		double expectedFail = expected/2;
//		double actual = powertrain.getOutputSpeed();
//
//		assertEquals("getOutputSpeed failed", expected, actual, 0);
//		assertNotEquals("getOutputSpeed succeded unexpectedly", expectedFail, actual, 0);
//	}
//
//	@Test
//	public void freeCurrentTest(){
//		double expected = 1.8;
//		double expectedFail = expected/2;
//		double actual = powertrain.getFreeCurrent();
//
//		assertEquals("getFreeCurrent failed", expected, actual, 0);
//		assertNotEquals("getFreeCurrent succeded unexpectedly", expectedFail, actual, 0);
//	}
//
//	@Test
//	public void stallCurrentTest(){
//		double expected = 105;
//		double expectedFail = expected/2;
//		double actual = powertrain.getStallCurrent();
//
//		assertEquals("getStallCurrent failed", expected, actual, 0);
//		assertNotEquals("getStallCurrent succeded unexpectedly", expectedFail, actual, 0);
//
//	}
//
//	@Test
//	public void stallTorqueTest(){
//		double expected = 2.6 * 11.23;
//		double expectedFail = expected/2;
//		double actual = powertrain.getStallTorque();
//
//		assertEquals("getStallTorque failed", expected, actual, 0);
//		assertNotEquals("getStallTorque succeded unexpectedly", expectedFail, actual, 0);
//
//	}
//
//}
