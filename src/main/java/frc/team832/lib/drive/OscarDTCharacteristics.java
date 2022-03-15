package frc.team832.lib.drive;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.team832.lib.motors.WheeledPowerTrain;

public class OscarDTCharacteristics {
	public final WheeledPowerTrain powertrain;
	public final double wheelbaseInches;
	public final SimpleMotorFeedforward leftFeedforward;
	public final SimpleMotorFeedforward rightFeedforward;
	public final double leftkP;
	public final double rightkP;

	public OscarDTCharacteristics(
			WheeledPowerTrain powertrain, double wheelbaseInches,
			SimpleMotorFeedforward leftFeedforward,
			SimpleMotorFeedforward rightFeedforward, 
			double leftkP, double rightkP) {
		this.powertrain = powertrain;
		this.wheelbaseInches = wheelbaseInches;
		this.leftFeedforward = leftFeedforward;
		this.rightFeedforward = rightFeedforward;
		this.leftkP = leftkP;
		this.rightkP = rightkP;
	}
}
