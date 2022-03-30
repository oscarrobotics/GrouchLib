package frc.team832.lib.motors;

import edu.wpi.first.math.util.Units;

public class WheeledPowerTrain extends Powertrain {

	public static final double METERS_SEC_TO_FEET_SEC = 3.28084;

	private final double m_wheelDiameterMeters;
	private double m_encoderRatio;

	/**
	 *
	 * @param gearbox Gearbox
	 * @param motor Motor type
	 * @param motorCount Amount of motors
	 * @param wheelDiameterInches Wheel diameter in inches
	 */
	public WheeledPowerTrain(Gearbox gearbox, Motor motor, int motorCount, double wheelDiameterInches) {
		super(gearbox, motor, motorCount);
		m_wheelDiameterMeters = Units.inchesToMeters(wheelDiameterInches);
		setEncoderRatioIndex(0);
	}

	public void setEncoderRatioIndex(int reductionIndex) {
		if (reductionIndex == 0) {
			m_encoderRatio = gearbox.getTotalReduction();
		} else if (reductionIndex > 0) {
			m_encoderRatio = gearbox.getReduction(reductionIndex - 1);
		}
	}

	public double getWheelCircumferenceMeters() {
		return m_wheelDiameterMeters * Math.PI;
	}

	public int getWheelTicksPerRev(int encoderCPR) {
		return (int) (encoderCPR / m_encoderRatio * m_wheelDiameterMeters);
	}

	public double calculateWheelRPMFromMotorRPM(double currentRpm) { return currentRpm / m_encoderRatio ; }

	public double calculateMotorRpmFromWheelRpm(double wheelRPM) {
		return wheelRPM / m_encoderRatio;
	}

	public double calculateTicksFromWheelDistance(double distanceMeters) {
		return calculateTicksFromPosition(distanceMeters / (getWheelCircumferenceMeters()));
	}

	public double calculateWheelDistanceMeters(double encoderRotations) {
		return (encoderRotations / m_encoderRatio) * getWheelCircumferenceMeters();
	}

	public double calculateMetersPerSec(double currentRpm) {
		return (calculateWheelRPMFromMotorRPM(currentRpm) * getWheelCircumferenceMeters()) / 60f ;
	}

	public double calculateFeetPerSec(double currentRpm) {
		return calculateMetersPerSec(currentRpm) * METERS_SEC_TO_FEET_SEC;
	}
	
	public double calculateTicksFromPosition(double targetPosition) {
		return targetPosition / m_encoderRatio;
	}
}
