package frc.team832.lib.power.monitoring;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj.RobotController;
import frc.team832.lib.power.PDSlot;

public class StallDetector {
	public class StallDetectorStatus {
		public boolean isStalled;
		public double stalledForMillis;
	}

	private int stallCurrent = 5;
	private int minStallMillis = 100;

	private StallDetectorStatus stallStatus = new StallDetectorStatus();
	private final MedianFilter currentFilter = new MedianFilter(40); // enough to keep 1 second of data when called every 25ms
	private final DoubleSupplier currentSupplier;
	
	private double stalledMillis = 0;
	private long lastRunMicros = 0;
	
	public StallDetector(PDSlot slot) {
		stallCurrent = slot.getBreakerRatedCurrent();
		currentSupplier = slot::getCurrentUsage;
	}

	public StallDetector(DoubleSupplier currentSupplier) {
		this.currentSupplier = currentSupplier;
	}

	public void setStallCurrent(int stallCurrent) {
		this.stallCurrent = stallCurrent;
	}

	public void setMinStallMillis(int minStallMillis) {
		this.minStallMillis = minStallMillis;
	}

	public void updateStallStatus() {
		double currentCurrent = currentFilter.calculate(currentSupplier.getAsDouble());
		long nowMicros = RobotController.getFPGATime();
		long elapsedMicros = nowMicros - lastRunMicros;

		if (currentCurrent >= stallCurrent) {
			stalledMillis += (elapsedMicros / 1000.0);
		} else {
			if (stalledMillis >= 0) {
				stalledMillis = 0;
			} else {
				stalledMillis -= (elapsedMicros / 1000.0);
			}
		}

		lastRunMicros = RobotController.getFPGATime();

		stallStatus.isStalled = stalledMillis >= minStallMillis;
		stallStatus.stalledForMillis = stalledMillis;
	}

	public StallDetectorStatus getStallStatus() {
		return stallStatus;
	}
}
