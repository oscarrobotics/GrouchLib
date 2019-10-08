//package frc.team832.GrouchLib.motors;
//
//import frc.team832.GrouchLib.util.OscarMath;
//import org.junit.*;
//import static org.junit.Assert.*;
//
//public class DTPowertrainTest {
//    private Motor motor = null;
//    private Gearbox gearbox = null;
//    private DTPowerTrain dtPowerTrain = null;
//
//    @Before
//    public void init() {
//        motor = Motors.NEO;
//        gearbox = new Gearbox((long)11.23);
//        dtPowerTrain = new DTPowerTrain(gearbox, motor, 2, 6);
//    }
//
//    @Test
//    public void freeSpeedCorrect() {
//        double expectedFreeSpeed = 13.23;
//        double actualFreeSpeed = OscarMath.round(dtPowerTrain.calculateFeetPerSec(motor.freeSpeed), 2);
//        assertEquals("Free Speed FAIL", expectedFreeSpeed, actualFreeSpeed, 0);
//    }
//
//    @Test
//    public void halfSpeedCorrect() {
//        double expectedHalfSpeed = 6.62;
//        double actualHalfSpeed = OscarMath.round(dtPowerTrain.calculateFeetPerSec(motor.freeSpeed/2.0), 2);
//        assertEquals("Half Free Speed FAIL", expectedHalfSpeed, actualHalfSpeed, 0);
//    }
//}
