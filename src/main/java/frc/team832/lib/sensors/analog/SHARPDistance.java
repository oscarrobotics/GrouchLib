package frc.team832.lib.sensors.analog;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.MedianFilter;

public class SHARPDistance {

    private enum FitType {
        FIT_POLY,
        FIT_POWER
    }

    private final AnalogInput analog;
    private final MedianFilter filter;

    private FitType fitType;
    private float[] polyCoeffs = new float[6];
    private float powerCoeffC, powerCoeffP;
    private int valMin, valMax;

    private static final float[] SZLF_Coeffs = {1734, -9.005f, 2.023E-2f, -2.251E-5f, 1.167E-8f, 2.037E-12f};

    /**
     * Class to get the distance reported by a
     * Sharp GP2Y0A60SZLF analog distance sensor
     * @param channel Analog channel the sensor is connected do
     * @param medianFilterSize Size of the MedianFilter to reduce noise
     */
    public SHARPDistance(int channel, int medianFilterSize) {
        this.analog = new AnalogInput(channel);
        filter = new MedianFilter(medianFilterSize);
        setPolyFitCoeffs(SZLF_Coeffs, 30, 875);
    }

    private void setPolyFitCoeffs(float[] coeffs, int valMin, int valMax) {
        fitType = FitType.FIT_POLY;
        System.arraycopy(coeffs, 0, polyCoeffs, 0, coeffs.length);
        this.valMin = valMin;
        this.valMax = valMax;
    }

    private void setPowerFitCoeffs(float C, float P, int valMin, int valMax) {
        fitType = FitType.FIT_POWER;
        powerCoeffC = C;
        powerCoeffP = P;
        this.valMin = valMin;
        this.valMax = valMax;
    }

    private double read() {
        int rawVal = getRawValue();
        double dist = 0;

        if (fitType == FitType.FIT_POLY) {
            dist += polyCoeffs[5] * Math.pow(rawVal, 5);
            dist += polyCoeffs[4] * Math.pow(rawVal, 4);
            dist += polyCoeffs[3] * Math.pow(rawVal, 3);
            dist += polyCoeffs[2] * Math.pow(rawVal, 2);
            dist += polyCoeffs[1] * rawVal;
            dist += polyCoeffs[0];
        } else if (fitType == FitType.FIT_POWER) {
            dist = powerCoeffC * Math.pow(rawVal, powerCoeffP);
        }

        return filter.calculate(dist);
    }

    public int getRawValue() {
        return analog.getValue();
    }

    public double getDistance() {
        return read();
    }
}
