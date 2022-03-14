package frc.team832.lib.motors;

import org.junit.jupiter.api.Test;

import edu.wpi.first.math.util.Units;

import static org.junit.jupiter.api.Assertions.*;

public class DTPowertrainTest {
    private static Motor m_motor = Motor.kFalcon500;
    private static Gearbox m_gearbox = new Gearbox(60.0 / 11.0, 32.0 / 16.0);
    private static WheeledPowerTrain m_wheeledPowerTrain = new WheeledPowerTrain(m_gearbox, m_motor, 2, 6.0);

    @Test
    public void freeSpeedCorrect() {
        double expectedFreeSpeed = 15.31;
        double actualFreeSpeed = m_wheeledPowerTrain.calculateFeetPerSec(m_motor.freeSpeed);

        assertEquals(expectedFreeSpeed, actualFreeSpeed, 0.009f, "Free Speed FAIL");
    }

    @Test
    public void metersPerSecondTest(){
        double expectedMetersPerSec = 2.19;
        double actualMeterPerSec = m_wheeledPowerTrain.calculateMetersPerSec(3000);

        assertEquals(expectedMetersPerSec, actualMeterPerSec, 0.009f, "Meters Per Second FAIL");
    }

    @Test
    public void feetPerSecondTest(){
        double expectedFeetPerSec = 12.96;
        double actualFeetPerSec = m_wheeledPowerTrain.calculateFeetPerSec(5400);

        assertEquals(expectedFeetPerSec, actualFeetPerSec, 0.009f, "Feet Per Second FAIL");
    }

    @Test
    public void calculateWheelDistanceMetersTest() {
        double expectedWheelDistanceMeters = 3.2916;
        double actualWheelDistanceMeters = m_wheeledPowerTrain.calculateWheelDistanceMeters(75);

        assertEquals(expectedWheelDistanceMeters, actualWheelDistanceMeters, 0.0001f, "Calculate Wheel Distance Meters FAIL");
    }
}
