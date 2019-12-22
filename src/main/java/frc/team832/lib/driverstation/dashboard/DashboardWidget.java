package frc.team832.lib.driverstation.dashboard;

public enum DashboardWidget {
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
}
