package frc.team832.lib.driverinput.oi;

import frc.team832.lib.driverinput.controllers.Attack3;
import frc.team832.lib.driverinput.controllers.Extreme3DPro;
import frc.team832.lib.driverinput.controllers.HIDControllerType;

import java.util.Arrays;
import java.util.Optional;
import java.util.Vector;

@SuppressWarnings("WeakerAccess")
public class SticksDriverOI implements DriverOI<Attack3, Extreme3DPro> {

    public static final Vector<HIDControllerType> requiredControllers = new Vector<>(Arrays.asList(HIDControllerType.kAttack3, HIDControllerType.kExtreme3DPro));

    public final Attack3 leftStick;
    public final Extreme3DPro rightStick;

    private final DriveAxesSupplier tankDriveAxes;
    private final DriveAxesSupplier arcadeDriveAxes;

    public SticksDriverOI() {
        leftStick = new Attack3(0);
        rightStick = new Extreme3DPro(1);

        tankDriveAxes = new DriveAxesSupplier(leftStick::getY, rightStick::getY);
        arcadeDriveAxes = new DriveAxesSupplier(leftStick::getY, rightStick::getX);
    }

    @Override
    public DriveAxesSupplier getTankDriveAxes() {
        return tankDriveAxes;
    }

    @Override
    public DriveAxesSupplier getArcadeDriveAxes() {
        return arcadeDriveAxes;
    }

    @Override
    public Attack3 getFirstController() {
        return leftStick;
    }

    @Override
    public Optional<Extreme3DPro> getSecondController() {
        return Optional.ofNullable(rightStick);
    }
}
