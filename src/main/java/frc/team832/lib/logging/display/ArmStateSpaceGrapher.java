package frc.team832.lib.logging.display;

import frc.team832.lib.logging.formats.ArmStateSpaceLogLine;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.None;

import java.util.List;

public class ArmStateSpaceGrapher {
	private final XYChart chart;

	@SuppressWarnings("SuspiciousNameCombination")
	public ArmStateSpaceGrapher(String title, List<ArmStateSpaceLogLine> dataPoints) {
		chart = new XYChartBuilder().xAxisTitle("Time (sec)").width(800).height(600).title(title).build();

		double[] timestamps = dataPoints.stream().mapToDouble(c -> c.fpgaTimestamp).toArray();
		double[] references = dataPoints.stream().mapToDouble(c -> c.reference).toArray();
		double[] positionStates = dataPoints.stream().mapToDouble(c -> c.positionState).toArray();
		double[] velocityStates = dataPoints.stream().mapToDouble(c -> c.velocityState).toArray();
		double[] inputs = dataPoints.stream().mapToDouble(c -> c.input).toArray();
		double[] positionOutputs = dataPoints.stream().mapToDouble(c -> c.positionOutput).toArray();
		double[] velocityOutputs = dataPoints.stream().mapToDouble(c -> c.velocityOutput).toArray();

		chart.setYAxisGroupTitle(0, "Radians");
		chart.setYAxisGroupTitle(1, "Rad/s");
		chart.setYAxisGroupTitle(2, "Volts");
		chart.getStyler().setYAxisMin(2, -13.0);
		chart.getStyler().setYAxisMax(2, 13.0);
		chart.getStyler().setYAxisGroupPosition(2, Styler.YAxisPosition.Right);
		chart.getStyler().setSeriesMarkers(new Marker[] {new None(), new None(), new None(), new None(), new None(), new None()});

		chart.addSeries("Reference", timestamps, references).setYAxisGroup(0);
		chart.addSeries("PositionState", timestamps, positionStates).setYAxisGroup(0);
		chart.addSeries("VelocityState", timestamps, velocityStates).setYAxisGroup(1);
		chart.addSeries("Input", timestamps, inputs).setYAxisGroup(2);
		chart.addSeries("PositionOutput", timestamps, positionOutputs).setYAxisGroup(0);
		chart.addSeries("VelocityOutput", timestamps, velocityOutputs).setYAxisGroup(1);
	}

	public void showGraph() {
		new SwingWrapper<>(chart).displayChart();
	}
}
