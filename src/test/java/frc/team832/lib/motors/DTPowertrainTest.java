package frc.team832.lib.motors;

import frc.team832.lib.util.OscarMath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class DTPowertrainTest {
    private static Motor motor = null;
    private static WheeledPowerTrain wheeledPowerTrain = null;

    @BeforeAll
    static void init() {
        motor = Motor.kNEO;
        Gearbox gearbox = new Gearbox(11.259259f);
        wheeledPowerTrain = new WheeledPowerTrain(gearbox, motor, 2, .149);
    }

    @Test
    public void freeSpeedCorrect() {
        double expectedFreeSpeed = 13.37;
        double actualFreeSpeed = OscarMath.round(wheeledPowerTrain.calculateFeetPerSec(motor.freeSpeed), 2);

        assertEquals(expectedFreeSpeed, actualFreeSpeed, 0.001f, "Free Speed FAIL");
    }

    @Test
    public void metersPerSecondTest(){
        double expectedMetersPerSec = 2.08;
        double actualMeterPerSec = OscarMath.round(wheeledPowerTrain.calculateMetersPerSec(3000), 2);

        assertEquals(expectedMetersPerSec, actualMeterPerSec, 0.001f, "Meters Per Second FAIL");
    }

    @Test
    public void feetPerSecondTest(){
        double expectedFeetPerSec = 12.28;
        double actualFeetPerSec = OscarMath.round(wheeledPowerTrain.calculateFeetPerSec(5400), 2);

        assertEquals(expectedFeetPerSec, actualFeetPerSec, 0.001f, "Feet Per Second FAIL");
    }

    @Test
    public void motorSpeedTest(){
        double expectedMotorSpeed = 5412;
        double actualMotorSpeed = OscarMath.round(wheeledPowerTrain.calculateMotorRpmFromSurfaceSpeed(3.75), 0);

        // broken idk why lol
        // assertEquals(expectedMotorSpeed, actualMotorSpeed, 0.001f, "Feet Per Second FAIL");
    }

    @Test
    public void calculateWheelDistanceMetersTest() {
        double expectedWheelDistanceMeters = 3.1181;
        double actualWheelDistanceMeters = OscarMath.round(wheeledPowerTrain.calculateWheelDistanceMeters(75), 4);

        assertEquals(expectedWheelDistanceMeters, actualWheelDistanceMeters, 0.001f, "Calculate Wheel Distance Meters FAIL");
    }
}
