package frc.team832.lib.drive;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import frc.team832.lib.motors.WheeledPowerTrain;

public class OscarDTCharacteristics {
	public final WheeledPowerTrain powertrain;
	public final double wheelbaseMeters;
	public final SimpleMotorFeedforward leftFeedforward;
	public final SimpleMotorFeedforward rightFeedforward;
	public final double leftkP;
	public final double rightkP;
	public final double massKg;
	public final double moiKgM2;

	public OscarDTCharacteristics(
			WheeledPowerTrain powertrain, double wheelbaseMeters,
			SimpleMotorFeedforward leftFeedforward,
			SimpleMotorFeedforward rightFeedforward, 
			double leftkP, double rightkP,
			double massKg, double moiKgM2	
		) {
		this.powertrain = powertrain;
		this.wheelbaseMeters = wheelbaseMeters;
		this.leftFeedforward = leftFeedforward;
		this.rightFeedforward = rightFeedforward;
		this.leftkP = leftkP;
		this.rightkP = rightkP;
		this.massKg = massKg;
		this.moiKgM2 = moiKgM2;
	}

	
}
