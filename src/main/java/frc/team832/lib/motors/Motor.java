package frc.team832.lib.motors;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;

public class Motor extends DCMotor {

	public final double freeSpeedRPM;
	public final double KvRPMPerVolt;

	public static Motor fromDCMotor(DCMotor wpilibMotor) {
		return new Motor(
			wpilibMotor.nominalVoltageVolts,
			wpilibMotor.stallTorqueNewtonMeters,
			wpilibMotor.stallCurrentAmps, 
			wpilibMotor.freeCurrentAmps,
			Units.radiansPerSecondToRotationsPerMinute(wpilibMotor.freeSpeedRadPerSec));
	}

	public Motor(
		double nominalVoltageVolts, double stallTorqueNewtonMeters,
		double stallCurrentAmps, double freeCurrentAmps,
		double freeSpeedRPM) {
		super(
			nominalVoltageVolts, stallTorqueNewtonMeters, 
			stallCurrentAmps, freeCurrentAmps,
			Units.rotationsPerMinuteToRadiansPerSecond(freeSpeedRPM), 1);

		this.freeSpeedRPM = Units.radiansPerSecondToRotationsPerMinute(freeSpeedRadPerSec);
		this.KvRPMPerVolt = Units.radiansPerSecondToRotationsPerMinute(KvRadPerSecPerVolt);
	}

	public Motor(Motor existingMotor, int motorCount) {
		this(
			existingMotor.nominalVoltageVolts,
			existingMotor.stallTorqueNewtonMeters * motorCount,
			existingMotor.stallCurrentAmps * motorCount,
			existingMotor.freeCurrentAmps * motorCount,
			existingMotor.freeSpeedRPM
		);
	}


	public static double predictiveCurrentLimit(DCMotor motor, double currentVolts, double maxI, double speedRPM) {
		double radsPerSec = Units.radiansPerSecondToRotationsPerMinute(speedRPM);
		double currentI = motor.getCurrent(radsPerSec, currentVolts);
		double outputV = currentVolts;

		if (currentI > maxI) {
			outputV = maxI * motor.rOhms + (radsPerSec / motor.KvRadPerSecPerVolt);
		}
		return outputV;
	}

	public double reactiveCurrentLimit(DCMotor motor, double currentV, double maxI, double currentI) {
		double omega = motor.KvRadPerSecPerVolt * currentV - currentI * motor.rOhms * motor.KvRadPerSecPerVolt;
		double outputV = currentV;

		if (currentI > maxI) {
			outputV = maxI * motor.rOhms + omega / motor.KvRadPerSecPerVolt;
		}
		return outputV;
	}

	public static final Motor kCIM = fromDCMotor(DCMotor.getCIM(1));
	public static final Motor kMiniCIM = fromDCMotor(DCMotor.getMiniCIM(1));
	public static final Motor kNEO = fromDCMotor(DCMotor.getNEO(1));
	public static final Motor kNEO550 = fromDCMotor(DCMotor.getNeo550(1));
	public static final Motor kFalcon500 = fromDCMotor(DCMotor.getFalcon500(1));
	public static final Motor kBAG = fromDCMotor(DCMotor.getBag(1));
	public static final Motor k775Pro = fromDCMotor(DCMotor.getVex775Pro(1));
	public static final Motor kAndyMark9015 = fromDCMotor(DCMotor.getAndymark9015(1));
	public static final Motor kAndyMarkNeveRest = new Motor(12, 5480, 0.4, 0.17, 10);
	public static final Motor kRS775_125 = fromDCMotor(DCMotor.getAndymarkRs775_125(1));
	public static final Motor kRS775_18V = fromDCMotor(DCMotor.getBanebotsRs775(1));
	public static final Motor kRS550 = fromDCMotor(DCMotor.getBanebotsRs550(1));
}