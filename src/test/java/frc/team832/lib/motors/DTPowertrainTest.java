package frc.team832.lib.motors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DTPowertrainTest {
    private static Motor m_motor = Motor.kFalcon500;
    private static Gearbox m_gearbox = new Gearbox(11.0 / 60.0, 16.0 / 32.0);
    private static WheeledPowerTrain m_wheeledPowerTrain = new WheeledPowerTrain(m_gearbox, m_motor, 2, 6.0, 1.0);

    @Test
    public void wheelSpeedFromMotorSpeedTest() {
        double motorRpm = 6380;
        double expectedWheelRpm = 584.833;
        double actualWheelRpm = m_wheeledPowerTrain.calcWheelFromMotor(motorRpm);

        assertEquals(expectedWheelRpm, actualWheelRpm, 0.001f, "Motor to wheel RPM FAIL!");
    }

    @Test
    public void motorSpeedFromWheelSpeedTest() {
        double wheelRpm = 580;
        double expectedMotorRpm = 6327.273;
        double actualMotorRpm = m_wheeledPowerTrain.calcMotorFromWheel(wheelRpm);

        assertEquals(expectedMotorRpm, actualMotorRpm, 0.001f, "Wheel to motor RPM FAIL!");
    }

    @Test
    public void freeSpeedCorrect() {
        double expectedFreeSpeed = 15.31;
        double encoderRpm = m_wheeledPowerTrain.calcWheelFromMotor(m_motor.freeSpeedRPM);
        double actualFreeSpeed = m_wheeledPowerTrain.calcFeetPerSec(encoderRpm);

        assertEquals(expectedFreeSpeed, actualFreeSpeed, 0.009f, "Free Speed FAIL");
    }

    @Test
    public void metersPerSecondTest(){
        double expectedMetersPerSec = 2.19;
        double encoderRpm = m_wheeledPowerTrain.calcWheelFromMotor(3000);
        double actualMeterPerSec = m_wheeledPowerTrain.calcMetersPerSec(encoderRpm);

        assertEquals(expectedMetersPerSec, actualMeterPerSec, 0.009f, "Meters Per Second FAIL");
    }

    @Test
    public void feetPerSecondTest(){
        double expectedFeetPerSec = 12.96;
        double encoderRpm = m_wheeledPowerTrain.calcWheelFromMotor(5400);
        double actualFeetPerSec = m_wheeledPowerTrain.calcFeetPerSec(encoderRpm);

        assertEquals(expectedFeetPerSec, actualFeetPerSec, 0.009f, "Feet Per Second FAIL");
    }

    @Test
    public void calculateWheelDistanceMetersTest() {
        double expectedWheelDistanceMeters = 0.479;
        double wheelRotations = m_wheeledPowerTrain.calcWheelFromEncoder(1);
        double actualWheelDistanceMeters = m_wheeledPowerTrain.calcWheelDistanceMeters(wheelRotations);

        assertEquals(expectedWheelDistanceMeters, actualWheelDistanceMeters, 0.001f, "Calculate Wheel Distance Meters FAIL");
    }

    @Test
    public void calcEncRotationsFromMetersTest() {
        double travelledMeters = 5;
        double expectedEncRotations = 113.926;
        double actualEncRotations = m_wheeledPowerTrain.calcEncoderRotationsFromMeters(travelledMeters);

        assertEquals(expectedEncRotations, actualEncRotations, 0.001f, "Calculate Encoder Rotations FAIL");
    }
}
