package frc.team832.lib.motors;

import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

public class DTPowerTrain extends Powertrain {

    public static final double METERS_SEC_TO_FEET_SEC = 3.28084;

    private final double _wheelRadiusMeters;
    private double _encoderRatio;

    /**
     *
     * @param gearbox Gearbox
     * @param motor Motor type
     * @param motorCount Amount of motors
     * @param wheelRadiusMeters Wheel radius in meters
     */
    public DTPowerTrain(Gearbox gearbox, Motor motor, int motorCount, double wheelRadiusMeters) {
        super(gearbox, motor, motorCount);
        _wheelRadiusMeters = wheelRadiusMeters;
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
        return (int) (encoderCPR * _encoderRatio * _wheelRadiusMeters);
    }

    public double getWheelRpm(double currentRpm) {
        return _encoderRatio * currentRpm;
    }

    public double calculateMetersPerSec(double currentRpm) {
        return _wheelRadiusMeters * Math.PI * getWheelRpm(currentRpm) / 60;
    }

    public double calculateFeetPerSec(double currentRpm) {
        return calculateMetersPerSec(currentRpm) * METERS_SEC_TO_FEET_SEC;
    }

    public double calculateMotorSpeed(double wheelMetersPerSec) {
        return wheelMetersPerSec * 60 / (_wheelRadiusMeters * Math.PI * _encoderRatio);
    }
}
