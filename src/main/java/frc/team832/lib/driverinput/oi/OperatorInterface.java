package frc.team832.lib.driverinput.oi;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team832.lib.driverinput.controllers.HIDControllerType;

import java.util.Vector;
@SuppressWarnings("unused")
public class OperatorInterface {
    private static Vector<HIDControllerType> attachedControllers = new Vector<>(6);

    private static final DriverStation driverStation = DriverStation.getInstance();
    private static int detectedStickCount = 0;

    private static void init() {
        for (int i = 0; i < DriverStation.kJoystickPorts; i++) {
            HIDControllerType thisController = getHIDControllerAtIndex(i);
            if (thisController != null) {
                detectedStickCount++;
                attachedControllers.add(i, thisController);
            }
        }
    }

    static {
        init();
    }

    public static int getConnectedControllerCount() {
        return detectedStickCount;
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
}
