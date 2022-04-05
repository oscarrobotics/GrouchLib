package frc.team832.lib.motors;

import edu.wpi.first.math.util.Units;

public class WheeledPowerTrain extends Powertrain {
	private final Motor m_wpilibPlantMotor;
	public final double encoderRatio;
	public final double wheelDiameterMeters;

	/**
	 *
	 * @param gearbox Gearbox
	 * @param motor Motor type
	 * @param motorCount Amount of motors
	 * @param wheelDiameterInches Wheel diameter in inches
	 * @param encoderRatio Ratio from encoder to wheel.
	 */
	public WheeledPowerTrain(Gearbox gearbox, Motor motor, int motorCount, double wheelDiameterInches, double encoderRatio) {
		super(gearbox, motor, motorCount);
		m_wpilibPlantMotor = new Motor(motor, motorCount);
		wheelDiameterMeters = Units.inchesToMeters(wheelDiameterInches);
		this.encoderRatio = encoderRatio;
	}

	/**
	 *	This constructor assumes the encoder is at the input of the gearbox.
	 * 
	 * @param gearbox Gearbox
	 * @param motor Motor type
	 * @param motorCount Amount of motors
	 * @param wheelDiameterInches Wheel diameter in inches
	 */
	public WheeledPowerTrain(Gearbox gearbox, Motor motor, int motorCount, double wheelDiameterInches) {
		super(gearbox, motor, motorCount);
		m_wpilibPlantMotor = new Motor(motor, motorCount);
		wheelDiameterMeters = Units.inchesToMeters(wheelDiameterInches);
		this.encoderRatio = 1;
	}

	public double getWheelCircumferenceMeters() {
		return wheelDiameterMeters * Math.PI;
	}

	public double calcWheelFromMotor(double motorUnits) {
		return gearbox.getInputToOutput(motorUnits);
	}

	public double calcMotorFromWheel(double wheelUnits) {
		return gearbox.getOutputToInput(wheelUnits);
	}

	public double calcWheelFromEncoder(double encoderUnits) {
		return encoderUnits / encoderRatio;
	}

	public double calcEncoderFromWheel(double wheelUnits) {
		return wheelUnits * encoderRatio;
	}

	public double calcMotorFromEncoder(double encoderUnits) {
		return calcMotorFromWheel(calcWheelFromEncoder(encoderUnits));
	}

	public double calcWheelDistanceMeters(double encoderRotations) {
		double wheelRotations = calcWheelFromEncoder(encoderRotations);
		double wheelCircumference = getWheelCircumferenceMeters();
		return wheelRotations * wheelCircumference;
	}

	public double calcEncoderRotationsFromMeters(double meters) {
		double wheelRotations = meters / getWheelCircumferenceMeters();
		double encoderRotations = calcEncoderFromWheel(wheelRotations);
		return encoderRotations;
	}

	public double calcEncoderRpmFromMetersPerSec(double metersPerSec) {
		double wheelRotationsPerSec = metersPerSec / getWheelCircumferenceMeters();
		double encoderRotationsPerSec = calcEncoderFromWheel(wheelRotationsPerSec);
		return encoderRotationsPerSec * 60;
	}

	public double calcMetersPerSec(double encoderRpm) {
		double wheelRpm = calcWheelFromEncoder(encoderRpm);
		return (wheelRpm * getWheelCircumferenceMeters()) / 60f ;
	}

	public double calcFeetPerSec(double currentMotorRpm) {
		return Units.metersToFeet(calcMetersPerSec(currentMotorRpm));
	}
	
	/**
	 * Returns a Motor modified to use the motor count, as WPILib uses in their system plants.
	 */
	public Motor getWPILibPlantMotor() {
		return m_wpilibPlantMotor;
	}
}
