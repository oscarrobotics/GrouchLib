package frc.team832.GrouchLib.driverstation.dashboard;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import java.util.HashMap;

public class DashboardManager {

	private static final String WIDGET_TEXTBOX = "Text Box";
	private static final String WIDGET_BOOLEAN = "Boolean Box";

	private DashboardManager() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	private static HashMap<DashboardUpdatable, ShuffleboardTab> shuffleboardTabs = new HashMap<>();

	public static void addTab(DashboardUpdatable updatable) {
		if (!shuffleboardTabs.containsKey(updatable)) {
			shuffleboardTabs.put(updatable, Shuffleboard.getTab(updatable.getDashboardTabName()));
		}
	}

	public static NetworkTableEntry addTabItem(DashboardUpdatable updatable, String itemName, Object defaultValue, DashboardWidget widget) {
		if (!shuffleboardTabs.containsKey(updatable)) return null;
		return shuffleboardTabs.get(updatable).add(itemName, defaultValue).withWidget(widget.name).getEntry();
	}

	public static NetworkTableEntry addTabItem(DashboardUpdatable updatable, String itemName, Object defaultValue) {
		return addTabItem(updatable, itemName, defaultValue, DashboardWidget.TextView);
	}

	public static void updateAllTabs() {
		shuffleboardTabs.forEach((key, value) -> key.updateDashboardData());
	}
}
