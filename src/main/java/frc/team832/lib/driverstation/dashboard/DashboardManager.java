package frc.team832.lib.driverstation.dashboard;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.WidgetType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DashboardManager {

	private DashboardManager() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static Map<String, Object> getRangePropertiesMap(double both) {
		return Collections.unmodifiableMap(Map.of("Min", -both, "Max", both));
	}

	public static Map<String, Object> getRangePropertiesMap(double min, double max) {
		return Collections.unmodifiableMap(Map.of("Min", min, "Max", max));
	}
	private static HashMap<String, ShuffleboardTab> shuffleboardTabs = new HashMap<>();

	public static void addTab(String tabName) {
		if (!shuffleboardTabs.containsKey(tabName)) {
			var tab = Shuffleboard.getTab(tabName);
			shuffleboardTabs.put(tabName, tab);
		}
	}

	public static void addTab(SubsystemBase subsystemBase) {
		if (!shuffleboardTabs.containsKey(subsystemBase.getName())) {
			var tab = Shuffleboard.getTab(subsystemBase.getName());
			shuffleboardTabs.put(subsystemBase.getName(), tab);
			tab.add(subsystemBase);
		}
	}

	public static NetworkTableEntry addTabItem(SubsystemBase subsystemBase, String itemName, Object defaultValue, WidgetType widget) {
		return addTabItem(subsystemBase.getName(), itemName, defaultValue, widget);
	}

	public static NetworkTableEntry addTabItem(String tabName, String itemName, Object defaultValue, WidgetType widget) {
		return addTabItem(tabName, itemName, defaultValue, widget, Map.of());
	}

	public static NetworkTableEntry addTabItem(String tabName, String itemName, Object defaultValue, WidgetType widget, Map<String, Object> properties) {
		if (!shuffleboardTabs.containsKey(tabName)) return null;
		return shuffleboardTabs.get(tabName).add(itemName, defaultValue).withWidget(widget).withProperties(properties).getEntry();
	}

	public static NetworkTableEntry addTabItem(String tabName, String itemName, Object defaultValue, WidgetType widget, int width, int height, Map<String, Object> properties) {
		if (!shuffleboardTabs.containsKey(tabName)) return null;
		return shuffleboardTabs.get(tabName).add(itemName, defaultValue).withWidget(widget).withSize(width, height).withProperties(properties).getEntry();
	}

	public static NetworkTableEntry addTabItem(SubsystemBase subsystemBase, String itemName, Object defaultValue) {
		return addTabItem(subsystemBase.getName(), itemName, defaultValue);
	}

	public static NetworkTableEntry addTabItem(String tabName, String itemName, Object defaultValue) {
		return addTabItem(tabName, itemName, defaultValue, BuiltInWidgets.kTextView);
	}

	public static NetworkTableEntry addTabNumberBar(String tabName, String itemName, double min, double max) {
		return addTabItem(tabName, itemName, 0.0, BuiltInWidgets.kNumberBar, getRangePropertiesMap(min, max));
	}

	public static void addTabButton(String tabName, String buttonName, Runnable onPress) {
		if (!shuffleboardTabs.containsKey(tabName)) return;
		InstantCommand command = new InstantCommand(onPress);
		command.setName(buttonName);
		shuffleboardTabs.get(tabName).add(command);
	}

	public static void addTabSendable(String tabName, String itemName, Sendable sendable) {
		if (!shuffleboardTabs.containsKey(tabName)) return;
		shuffleboardTabs.get(tabName).add(itemName, sendable);
	}

	public static <E extends Enum<E>> SendableChooser<E> addTabChooser(String tabName, String chooserName, E[] choosable, E defaultChosen) {
		if (!shuffleboardTabs.containsKey(tabName)) return null;

		SendableChooser<E> chooser = new SendableChooser<>();

		for (E _enum : choosable) {
			if (_enum == defaultChosen) {
				chooser.setDefaultOption(_enum.name(), _enum);
			} else {
				chooser.addOption(_enum.name(), _enum);
			}
		}

		getTab(tabName).add(chooserName, chooser);

		return chooser;
	}

	public static ShuffleboardTab getTab(String tabName) {
		return shuffleboardTabs.get(tabName);
	}

	public static ShuffleboardTab getTab(SubsystemBase subsystemBase) {
		return shuffleboardTabs.get(subsystemBase.getName());
	}
}
