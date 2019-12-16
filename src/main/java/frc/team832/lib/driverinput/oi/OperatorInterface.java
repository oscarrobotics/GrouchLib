package frc.team832.lib.driverinput.oi;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team832.lib.driverinput.controllers.HIDControllerType;

import java.util.Vector;
@SuppressWarnings("unused")
public class OperatorInterface {
    private static Vector<HIDControllerType> attachedControllers = new Vector<>();

    private static final DriverStation driverStation = DriverStation.getInstance();

    private static void init() {
        for (int i = 0; i < DriverStation.kJoystickPorts; i++) {
            HIDControllerType thisController = getHIDControllerAtIndex(i);
            if (thisController != null) {
                attachedControllers.add(i, thisController);
            }
        }
    }

    static {
        init();
    }

    private static HIDControllerType getHIDControllerAtIndex(int index) {
        int buttonCount = driverStation.getStickButtonCount(index);
        int axisCount = driverStation.getStickAxisCount(index);
        boolean hasPOV = driverStation.getStickPOVCount(index) > 0;

        return HIDControllerType.getFromStickData(buttonCount, axisCount, hasPOV);
    }

    public static void refresh() {
        for (HIDControllerType controller : attachedControllers) {
            int index = attachedControllers.indexOf(controller);
            HIDControllerType newController = getHIDControllerAtIndex(index);
            attachedControllers.set(index, newController);
        }
    }

    public static DriverOI getDriverOIForAttached() {
        // TODO: check that this also matches indexes
        if (attachedControllers.containsAll(SticksDriverOI.requiredControllers)) {
            return new SticksDriverOI();
        } else if (attachedControllers.containsAll(XboxDriverOI.requiredControllers)) {
            return new XboxDriverOI();
        }
        else return null;
    }
}
