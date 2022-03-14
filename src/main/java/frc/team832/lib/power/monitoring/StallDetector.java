package frc.team832.lib.power.monitoring;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.filter.MedianFilter;
import frc.team832.lib.power.PDSlot;

public class StallDetector {
	public class StallDetectorStatus {
		public boolean isStalled;
		public int stalledForMillis;
	}

	private int stallCurrent = 5;
	private int minStallMillis = 100;

	private StallDetectorStatus stallStatus = new StallDetectorStatus();
	private final MedianFilter currentFilter = new MedianFilter(40); // enough to keep 1 second of data when called every 25ms
	private final DoubleSupplier currentSupplier;
	
	private long stallMillis;
	private long lastRunMillis;
	
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
		long nowMillis = System.currentTimeMillis();
		long elapsed = nowMillis - lastRunMillis;

		if (currentCurrent >= stallCurrent) {
			stallMillis += elapsed;
		} else {
			stallMillis -= elapsed;
		}

		lastRunMillis = System.currentTimeMillis();

		stallStatus.isStalled = stallMillis >= minStallMillis;
		stallStatus.stalledForMillis = (int) stallMillis;
	}

	public StallDetectorStatus getStallStatus() {
		return stallStatus;
	}
}
