package frc.team832.lib.driverstation.dashboard;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.HashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DashboardManager {

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

    public static void addTabSendable(DashboardUpdatable updatable, String itemName, Sendable sendable) {
        if (!shuffleboardTabs.containsKey(updatable)) return;
        shuffleboardTabs.get(updatable).add(itemName, sendable);
    }

    public static <E extends Enum<E>> SendableChooser<E> addTabChooser(DashboardUpdatable updatable, String chooserName, E[] choosable, E defaultChosen) {
        if (!shuffleboardTabs.containsKey(updatable)) return null;

        SendableChooser<E> chooser = new SendableChooser<>();

		for (E _enum : choosable) {
			if (_enum == defaultChosen) {
				chooser.setDefaultOption(_enum.name(), _enum);
			} else {
				chooser.addOption(_enum.name(), _enum);
			}
		}

        getTab(updatable).add(chooserName, chooser);

        return chooser;
    }

    public static ShuffleboardTab getTab(DashboardUpdatable updatable) {
        return shuffleboardTabs.get(updatable);
    }

    public static void updateAllTabs() {
        shuffleboardTabs.forEach((key, value) -> key.updateDashboardData());
    }
}
