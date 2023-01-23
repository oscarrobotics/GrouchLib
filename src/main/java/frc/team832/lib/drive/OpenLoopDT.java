 package frc.team832.lib.drive;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.team832.lib.motors.WheeledPowerTrain;

public class OpenLoopDT {
	public final SimpleMotorFeedforward leftFeedforward;
	public final SimpleMotorFeedforward rightFeedforward;
	public final WheeledPowerTrain powerTrain;
	private double ffAcceleration;

	public OpenLoopDT(SimpleMotorFeedforward leftFeedforward, SimpleMotorFeedforward rightFeedforward, WheeledPowerTrain powerTrain) {
		this.leftFeedforward = leftFeedforward;
		this.rightFeedforward = rightFeedforward;
		this.powerTrain = powerTrain;
	}

	public double getFFAccel() {
		return this.ffAcceleration;
	}

	public void setFFAccel(double accel) {
		ffAcceleration = accel;
	}
}
