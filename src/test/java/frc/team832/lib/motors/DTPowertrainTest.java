package frc.team832.lib.motors;

import frc.team832.lib.util.OscarMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DTPowertrainTest {
    private Motor motor = null;
    private DTPowerTrain dtPowerTrain = null;

    @Before
    public void init() {
        motor = Motor.kNEO;
        Gearbox gearbox = new Gearbox(11.259259f);
        dtPowerTrain = new DTPowerTrain(gearbox, motor, 2, .149);
    }

    @Test
    public void freeSpeedCorrect() {
        double expectedFreeSpeed = 13.37;
        double actualFreeSpeed = OscarMath.round(dtPowerTrain.calculateFeetPerSec(motor.freeSpeed), 2);

        assertEquals("Free Speed FAIL", expectedFreeSpeed, actualFreeSpeed, 0);
    }

    @Test
    public void metersPerSecondTest(){
        double expectedMetersPerSec = 2.08;
        double actualMeterPerSec = OscarMath.round(dtPowerTrain.calculateMetersPerSec(3000), 2);

        assertEquals("Meters Per Second FAIL", expectedMetersPerSec, actualMeterPerSec, 0);
    }

    @Test
    public void feetPerSecondTest(){
        double expectedFeetPerSec = 12.28;
        double actualFeetPerSec = OscarMath.round(dtPowerTrain.calculateFeetPerSec(5400), 2);

        assertEquals("Feet Per Second FAIL", expectedFeetPerSec, actualFeetPerSec, 0);
    }

    @Test
    public void motorSpeedTest(){
        double expectedMotorSpeed = 5412;
        double actualMotorSpeed = OscarMath.round(dtPowerTrain.calculateMotorSpeed(3.75), 0);

        assertEquals("Feet Per Second FAIL", expectedMotorSpeed, actualMotorSpeed, 0);
    }

    @Test
    public void calculateWheelDistanceMetersTest() {
        double expectedWheelDistanceMeters = 3.1181;
        double actualWheelDistanceMeters = OscarMath.round(dtPowerTrain.calculateWheelDistanceMeters(75), 4);

        assertEquals("Calculate Wheel Distance Meters FAIL", expectedWheelDistanceMeters, actualWheelDistanceMeters, 0);
    }
}
