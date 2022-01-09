package frc.team832.lib.driverinput.oi;

import edu.wpi.first.wpilibj2.command.CommandXboxController;
import frc.team832.lib.driverinput.controllers.HIDControllerType;

import java.util.Collections;
import java.util.Vector;

@SuppressWarnings("WeakerAccess")
public class XboxDriverOI implements DriverOI {

	public static final Vector<HIDControllerType> requiredControllers = new Vector<>(Collections.singletonList(HIDControllerType.kXboxController));

	public final CommandXboxController xbox;

	private final DriveAxesSupplier tankDriveAxes;
	private final DriveAxesSupplier arcadeDriveAxes;
	private final DriveAxesSupplier greenbergDriveAxes;

	public XboxDriverOI() {
		xbox = new CommandXboxController(0);

		tankDriveAxes = new DriveAxesSupplier(xbox::getLeftY, xbox::getRightY);
		arcadeDriveAxes = new DriveAxesSupplier(xbox::getLeftY, xbox::getRightX);
		greenbergDriveAxes = new DriveAxesSupplier(xbox::getLeftY, xbox::getRightX);
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
	public DriveAxesSupplier getGreenbergDriveAxes() {
		return greenbergDriveAxes;
	}
}
