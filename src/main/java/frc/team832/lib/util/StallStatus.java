package frc.team832.lib.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team832.lib.control.PDP;

public class StallStatus {

	public static int stallCounter = 0;
	public static StallState spindexerStallState = StallState.NOT_STALLED;
	public static boolean hasStalled;
	public static boolean hasHatch;
	public static PDP pdp;

	public StallStatus(PDP pdp) {
		this.pdp = pdp;
	}

	public static StallState isStalling(int PDPSlot, double stallCurrent, double stallSec) {
		int slowdownMultiplier = 8;
		int  stallLoops = (int)(stallSec * 20);
		stallLoops *= slowdownMultiplier;
		StallState stallState = StallState.NOT_STALLED;
		double motorCurrent = pdp.getChannelCurrent(PDPSlot);

		SmartDashboard.putNumber("Stall Count", stallCounter);
		SmartDashboard.putNumber("Stall Loops", stallLoops);
		if (motorCurrent >= stallCurrent) {
			stallCounter += slowdownMultiplier;
		} else if (motorCurrent < stallCurrent) {
			stallCounter--;
		}

		stallCounter = OscarMath.clip(stallCounter, 0, stallLoops + 1);
		if (stallCounter >= stallLoops) {
			hasStalled = true;
			stallState = StallState.STALLED;
		} else if (stallCounter == 0) {
			hasStalled = false;
			stallState = StallState.NOT_STALLED;
		} else if (hasStalled & stallCounter < stallLoops / 4) {
			stallState = StallState.LEAVING_STALL;
		}
		return stallState;
	}

	public static void resetStall(){
		stallCounter = 0;
		spindexerStallState = StallState.NOT_STALLED;
	}

	public enum StallState {
		STALLED,
		LEAVING_STALL,
		NOT_STALLED
	}
}
