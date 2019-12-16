package frc.team832.lib.driverinput.oi;

import edu.wpi.first.wpilibj.GenericHID;
import frc.team832.lib.driverinput.controllers.HIDControllerType;
import frc.team832.lib.driverinput.controllers.Xbox360Controller;

import java.util.Collections;
import java.util.Optional;
import java.util.Vector;

@SuppressWarnings("WeakerAccess")
public class XboxDriverOI implements DriverOI<Xbox360Controller, Void> {

    public static final Vector<HIDControllerType> requiredControllers = new Vector<>(Collections.singletonList(HIDControllerType.kXbox360Controller));

    public final Xbox360Controller xbox;

    private final DriveAxesSupplier tankDriveAxes;
    private final DriveAxesSupplier arcadeDriveAxes;

    public XboxDriverOI() {
        xbox = new Xbox360Controller(0);

        tankDriveAxes = new DriveAxesSupplier(() -> xbox.getY(GenericHID.Hand.kLeft), () -> xbox.getY(GenericHID.Hand.kRight));
        arcadeDriveAxes = new DriveAxesSupplier(() -> xbox.getY(GenericHID.Hand.kLeft), () -> xbox.getX(GenericHID.Hand.kRight));
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
    public Xbox360Controller getFirstController() {
        return xbox;
    }

    @Override
    public Optional<Void> getSecondController() {
        return Optional.empty();
    }
}
