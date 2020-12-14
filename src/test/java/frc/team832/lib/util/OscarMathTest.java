package frc.team832.lib.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OscarMathTest {

    @Test
    public void roundTest() {
        double[] expectedValues = {13.23, 6.616, 3.30804};
        double[] actualValues = {0,0,0};

        actualValues[0] = OscarMath.round(13.23217564691, 2);
        actualValues[1] = OscarMath.round(6.616087823455, 3);
        actualValues[2] = OscarMath.round(3.3080439117275, 5);

        assertArrayEquals(expectedValues, actualValues, 0, "Rounding failed!");
    }

    @Test
    public void inRangeDoubleTest() {
        double min = 0.01;
        double max = 0.5;
        double validValue = 0.25;
        double invalidValue = 0.6;

        boolean valid = OscarMath.inRange(validValue, min, max);
        boolean invalid = OscarMath.inRange(invalidValue, min, max);

        assertTrue(valid, "InRange \"double\" failed on valid!");
        assertFalse(invalid, "InRange \"double\" failed on invalid!");
    }

    @Test
    public void inRangeIntegerTest() {
        int min = 0;
        int max = 5;
        int validValue = 3;
        int invalidValue = 7;

        boolean valid = OscarMath.inRange(validValue, min, max);
        boolean invalid = OscarMath.inRange(invalidValue, min, max);

        assertTrue(valid, "InRange \"int\" failed on valid!");
        assertFalse(invalid, "InRange \"int\" failed on invalid!");
    }

    @Test
    public void mapDoubleTest() {
        double value = 0.5;
        double inMin = -1.0;
        double inMax = 1.0;
        double outMin = 0.0;
        double outMax = 1.0;
        double expectedResult = 0.75;

        double actualResult = OscarMath.map(value, inMin, inMax, outMin, outMax);

        assertEquals(expectedResult, actualResult, 0, "Map \"double\" failed!");
    }

    @Test
    public void mapIntegerTest() {
        int value = 5;
        int inMin = -10;
        int inMax = 10;
        int outMin = 0;
        int outMax = 10;
        int expectedResult = 8;

        int actualResult = OscarMath.map(value, inMin, inMax, outMin, outMax);

        assertEquals(expectedResult, actualResult, 0, "Map \"int\" failed!");
    }

    @Test
    public void clipDoubleTest() {
        double value1 = -1.2;
        double value2 = 0.8;
        double min = -1.0;
        double max = 1.0;

        double expectedResult1 = -1.0;
        double expectedResult2 = 0.8;

        double actualResult1 = OscarMath.clip(value1, min, max);
        double actualResult2 = OscarMath.clip(value2, min, max);

        assertEquals(expectedResult1, actualResult1, 0, "Clip \"double\" failed!");
        assertEquals(expectedResult2, actualResult2, 0, "Clip \"double\" failed!");
    }

    @Test
    public void clipIntegerTest() {
        double value1 = -2019;
        double value2 = 512;
        double min = -1023;
        double max = 1023;

        double expectedResult1 = -1023;
        double expectedResult2 = 512;

        double actualResult1 = OscarMath.clip(value1, min, max);
        double actualResult2 = OscarMath.clip(value2, min, max);

        assertEquals(expectedResult1, actualResult1, 0, "Clip \"int\" failed!");
        assertEquals(expectedResult2, actualResult2, 0, "Clip \"int\" failed!");
    }

    @Test
    public void clipMapDoubleTest() {
        double value1 = -1.2;
        double value2 = 0.8;
        double inMin = -1.0;
        double inMax = 1.0;
        double outMin = 0.0;
        double outMax = 1.0;

        double expectedResult1 = 0.0;
        double expectedResult2 = 0.9;

        double actualResult1 = OscarMath.clipMap(value1, inMin, inMax, outMin, outMax);
        double actualResult2 = OscarMath.clipMap(value2, inMin, inMax, outMin, outMax);

        assertEquals(expectedResult1, actualResult1, 0, "ClipMap \"double\" failed!");
        assertEquals(expectedResult2, actualResult2, 0, "ClipMap \"double\" failed!");
    }

    @Test
    public void midDoubleTest() {
        double lower = 0.5;
        double upper = 1.0;

        double expectedResult = 0.75;

        double actualResult1 = OscarMath.mid(lower, upper);
        double actualResult2 = OscarMath.mid(upper, lower);

        assertEquals(expectedResult, actualResult1, 0, "Mid \"double\" failed!");
        assertEquals(expectedResult, actualResult2, 0, "Mid \"double\" inverse failed!");
    }

    @Test
    public void midIntegerTest() {
        int lower = 50;
        int upper = 100;

        int expectedResult = 75;

        int actualResult1 = OscarMath.mid(lower, upper);
        int actualResult2 = OscarMath.mid(upper, lower);

        assertEquals(expectedResult, actualResult1, 0, "Mid \"int\" failed!");
        assertEquals(expectedResult, actualResult2, 0, "Mid \"int\" inverse failed!");
    }

    @Test
    public void signumPowTest() {
        double value = -0.5;
        double power = 2;

        double expectedResult = -0.25;

        double actualResult = OscarMath.signumPow(value, power);

        assertEquals(expectedResult, actualResult, 0, "SignumPow \"double\" failed!");
    }

    @Test
    public void keepSignedPowTest() {
        double value1 = -0.5;
        double value2 = 0.5;

        double power = 1.5;

        double expectedResult1 = -0.3535533905932738;
        double expectedResult2 = 0.3535533905932738;

        double actualResult1 = OscarMath.keepSignedPow(value1, power);
        double actualResult2 = OscarMath.keepSignedPow(value2, power);

        assertEquals(expectedResult1, actualResult1, 0, "KeepSignedPow failed!");
        assertEquals(expectedResult2, actualResult2, 0, "KeepSignedPow failed!");
    }
}
