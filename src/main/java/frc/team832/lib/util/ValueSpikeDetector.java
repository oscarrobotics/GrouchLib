package frc.team832.lib.util;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj.Timer;

public class ValueSpikeDetector {

	private final DoubleSupplier m_valueSupplier;
	private final MedianFilter m_valueFilter;

	private int m_minSpikeLengthMillis;
	private int m_spikeThreshold;

	private int m_spikeDurationMillis;
	private double m_lastCalculateSecs;

	public ValueSpikeDetector(
		DoubleSupplier valueSupplier,
		int filterWindow,
		int spikeLengthMillis,
		int spikeThreshold
	) {
		m_valueSupplier = valueSupplier;
		m_valueFilter = new MedianFilter(filterWindow);

		m_minSpikeLengthMillis = spikeLengthMillis;
		m_spikeThreshold = spikeThreshold;
	}

	/**
	 * 
	 * @return Whether or not the spike was detected
	 */
	public boolean calculate() {
		double filteredValue = m_valueFilter.calculate(m_valueSupplier.getAsDouble());
		var secondsNow = Timer.getFPGATimestamp();
		var millisElapsed = (secondsNow - m_lastCalculateSecs) / 1000.0;

		if (filteredValue >= m_spikeThreshold) {
			m_spikeDurationMillis += millisElapsed;
		} else if (m_spikeDurationMillis > 0) {
			m_spikeDurationMillis -= millisElapsed;

			if (m_spikeDurationMillis < 0) m_spikeDurationMillis = 0;
		}

		m_lastCalculateSecs = Timer.getFPGATimestamp();

		return m_spikeDurationMillis >= m_minSpikeLengthMillis;
	}
}
