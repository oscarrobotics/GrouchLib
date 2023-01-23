package frc.team832.lib.driverinput.oi;

import frc.team832.lib.driverinput.controllers.Attack3;
import frc.team832.lib.driverinput.controllers.Extreme3DPro;
import frc.team832.lib.driverinput.controllers.HIDControllerType;

import java.util.Arrays;
import java.util.Vector;

public class SticksDriverOI implements DriverOI {

	public static final Vector<HIDControllerType> requiredControllers = new Vector<>(Arrays.asList(HIDControllerType.kAttack3, HIDControllerType.kExtreme3DPro));

	public final Attack3 leftStick;
	public final Extreme3DPro rightStick;

	private final DriveAxesSupplier tankDriveAxes;
	private final DriveAxesSupplier arcadeDriveAxes;
	private final DriveAxesSupplier greenbergDriveAxes;

	public SticksDriverOI() {
		leftStick = new Attack3(0);
		rightStick = new Extreme3DPro(1);

		tankDriveAxes = new DriveAxesSupplier(leftStick::getY, rightStick::getY, rightStick::getTwist);
		arcadeDriveAxes = new DriveAxesSupplier(leftStick::getY, rightStick::getX);
		greenbergDriveAxes = new DriveAxesSupplier(leftStick::getY, rightStick::getY, rightStick::getX, rightStick::getTwist);
	}

	@Override
	public DriveAxesSupplier getTankDriveAxes() {
		return tankDriveAxes;
	}

	@Override
	public DriveAxesSupplier getArcadeDriveAxes() {
		return arcadeDriveAxes;
	}

	public DriveAxesSupplier getGreenbergDriveAxes() {
		return greenbergDriveAxes;
	}
}
