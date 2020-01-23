package frc.team832.lib.motors;

public class WheeledPowerTrain extends Powertrain {

    public static final double METERS_SEC_TO_FEET_SEC = 3.28084;

    private final double _wheelDiameterMeters;
    private double _encoderRatio;

    /**
     *
     * @param gearbox Gearbox
     * @param motor Motor type
     * @param motorCount Amount of motors
     * @param wheelDiameterMeters Wheel diameter in meters
     */
    public WheeledPowerTrain (Gearbox gearbox, Motor motor, int motorCount, double wheelDiameterMeters) {
        super(gearbox, motor, motorCount);
        _wheelDiameterMeters = wheelDiameterMeters;
        setEncoderRatioIndex(0);
    }

    public void setEncoderRatioIndex(int reductionIndex) {
        if (reductionIndex == 0) {
            _encoderRatio = 1 / gearbox.getTotalReduction();
        } else if (reductionIndex > 0) {
            _encoderRatio = 1 / gearbox.getReduction(reductionIndex - 1);
        }
    }

    public int getWheelTicksPerRev(int encoderCPR) {
        return (int) (encoderCPR * _encoderRatio * _wheelDiameterMeters);
    }

    public double getWheelRpm(double currentRpm) {
        return _encoderRatio * currentRpm;
    }

    public double calculateMotorRpmFromWheelRpm(double targetRpm) {
        return targetRpm / _encoderRatio;
    }

    public double calculateMotorRpmFromSurfaceSpeed (double surfaceSpeedMetersPersec) { return (surfaceSpeedMetersPersec * 60f) / (Math.PI * _wheelDiameterMeters); }

    public double getWheelCircumferenceMeters() {
        return _wheelDiameterMeters * Math.PI;
    }

    public double calculateWheelDistanceMeters(double encoderRotations) {
        return encoderRotations * _encoderRatio * getWheelCircumferenceMeters();
    }

    public double calculateMetersPerSec(double currentRpm) {
        return (getWheelRpm(currentRpm) * Math.PI * _wheelDiameterMeters) / 60f ;
    }

    public double calculateFeetPerSec(double currentRpm) {
        return calculateMetersPerSec(currentRpm) * METERS_SEC_TO_FEET_SEC;
    }

    public double calculateMotorSpeed(double wheelMetersPerSec) {
        return wheelMetersPerSec * 60 / (_wheelDiameterMeters * Math.PI * _encoderRatio);
    }
}
