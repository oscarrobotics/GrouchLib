package frc.team832.lib.driverinput.controllers;

import java.util.List;

public enum HIDControllerType {
    kAttack3(Attack3.class, 11, 3),
    kExtreme3DPro(Extreme3DPro.class, 12, 4, true),
    kStratComInterface(StratComInterface.class, 19, 2),
    kSWPrecisionPro(SWPrecisionPro.class, 12, 4, true),
    kXbox360Controller(Xbox360Controller.class, 10, 6, true);

    public final Class hidClass;
    public final int buttonCount;
    public final int axisCount;
    public final boolean hasPOV;

    HIDControllerType(Class hidClass, int buttonCount, int axisCount) {
        this.hidClass = hidClass;
        this.buttonCount = buttonCount;
        this.axisCount = axisCount;
        this.hasPOV = false;
    }

    HIDControllerType(Class hidClass, int buttonCount, int axisCount, boolean hasPOV) {
        this.hidClass = hidClass;
        this.buttonCount = buttonCount;
        this.axisCount = axisCount;
        this.hasPOV = hasPOV;
    }

    public static HIDControllerType getFromStickData(int buttonCount, int axisCount, boolean hasPOV) {
        return List.of(HIDControllerType.values()).stream().filter(c -> c.buttonCount == buttonCount && c.axisCount == axisCount && c.hasPOV == hasPOV).findFirst().orElse(null);
    }
}
