package frc.team832.lib;

import edu.wpi.first.wpilibj.DriverStation;

public class Debug {
	private Debug() {}

	public static void showDSError(String error) {
		DriverStation.reportError(error, true);
	}

	public static void showDSWarning(String warning) {
		DriverStation.reportError(warning, false);
	}
}
