package frc.team832.lib.motors;

public class DTPowerTrain extends Powertrain {

    private final double _wheelSize;
    private double _encoderRatio;

    public DTPowerTrain(Gearbox gearbox, Motor motor, int motorCount, double wheelSize) {
        super(gearbox, motor, motorCount);
        _wheelSize = wheelSize;
        setEncoderRatioIndex(0);
    }

    public void setEncoderRatioIndex(int reductionIndex) {
        if (reductionIndex == 0) {
            _encoderRatio = 1 / gearbox.getTotalReduction();
        } else if (reductionIndex > 0) {
            _encoderRatio = 1 / gearbox.getReduction(reductionIndex - 1);
        }
    }

    public double getWheelRpm(double currentRpm) {
        return _encoderRatio * currentRpm;
    }

    public double calculateFeetPerSec(double currentRpm) {
        return _wheelSize * Math.PI / 12 * getWheelRpm(currentRpm) / 60;
    }
}
