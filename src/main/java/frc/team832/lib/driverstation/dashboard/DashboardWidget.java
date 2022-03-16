package frc.team832.lib.driverstation.dashboard;

import edu.wpi.first.wpilibj.shuffleboard.WidgetType;

/**
 * @deprecated To be removed in favor of {@link edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets}
 */
@Deprecated
public enum DashboardWidget implements WidgetType {
	TextView("Text View"),
	BooleanBox("Boolean Box"),
	ToggleButton("Toggle Button"),
	ToggleSwitch("Toggle Switch"),
	NumberBar("Number Bar"),
	NumberSlider("Number Slider"),
	Graph("Graph");

	public final String name;

	DashboardWidget(String name) {
		this.name = name;
	}

	@Override
	public String getWidgetName() {
		return name;
	}
}
