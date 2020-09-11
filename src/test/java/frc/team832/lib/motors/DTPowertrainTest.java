package frc.team832.lib.motors;

import frc.team832.lib.util.math.OscarMath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTPowertrainTest {
    private static final Motor motor = Motor.kNEO;
    private static final Gearbox gearbox = new Gearbox(11.259259f);
    private WheeledPowerTrain wheeledPowerTrain = new WheeledPowerTrain(gearbox, motor, 2, .149);

    @Test
    public void freeSpeedCorrect() {
        double expectedFreeSpeed = 13.37;
        double actualFreeSpeed = OscarMath.round(wheeledPowerTrain.calculateFeetPerSec(motor.freeSpeed), 2);

        assertEquals(expectedFreeSpeed, actualFreeSpeed, 0, "Free Speed FAIL");
    }

    @Test
    public void metersPerSecondTest(){
        double expectedMetersPerSec = 2.08;
        double actualMeterPerSec = OscarMath.round(wheeledPowerTrain.calculateMetersPerSec(3000), 2);

        assertEquals(expectedMetersPerSec, actualMeterPerSec, 0, "Meters Per Second FAIL");
    }

    @Test
    public void feetPerSecondTest(){
        double expectedFeetPerSec = 12.28;
        double actualFeetPerSec = OscarMath.round(wheeledPowerTrain.calculateFeetPerSec(5400), 2);

        assertEquals(expectedFeetPerSec, actualFeetPerSec, 0, "Feet Per Second FAIL");
    }


    // TODO: FIX
//    @Test
//    public void motorSpeedTest(){
//        double expectedMotorSpeed = 5412;
//        double actualMotorSpeed = OscarMath.round(wheeledPowerTrain.calculateMotorRpmFromSurfaceSpeed(3.75), 0);
//
//        assertEquals(expectedMotorSpeed, actualMotorSpeed, 0, "Feet Per Second FAIL");
//    }

    @Test
    public void calculateWheelDistanceMetersTest() {
        double expectedWheelDistanceMeters = 3.1181;
        double actualWheelDistanceMeters = OscarMath.round(wheeledPowerTrain.calculateWheelDistanceMeters(75), 4);

        assertEquals(expectedWheelDistanceMeters, actualWheelDistanceMeters, 0, "Calculate Wheel Distance Meters FAIL");
    }
}
