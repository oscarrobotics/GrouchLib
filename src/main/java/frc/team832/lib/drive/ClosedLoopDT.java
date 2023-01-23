package frc.team832.lib.drive;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.team832.lib.motors.WheeledPowerTrain;
import frc.team832.lib.util.ClosedLoopConfig;

public class ClosedLoopDT extends OpenLoopDT {
	public final ClosedLoopConfig rightConfig, leftConfig;

	public ClosedLoopDT(SimpleMotorFeedforward leftFeedforward, SimpleMotorFeedforward rightFeedforward,
						ClosedLoopConfig leftConfig, ClosedLoopConfig rightConfig, WheeledPowerTrain powerTrain) {
		super(leftFeedforward, rightFeedforward, powerTrain);
		this.leftConfig = leftConfig;
		this.rightConfig = rightConfig;
	}
}
