package frc.team832.lib.driverinput.oi;

import java.util.Optional;

public class EmptyDriverOI implements DriverOI {
	@Override
	public DriveAxesSupplier getTankDriveAxes () {
		return new DriveAxesSupplier(() -> 0, () -> 0);
	}

	@Override
	public DriveAxesSupplier getArcadeDriveAxes () {
		return new DriveAxesSupplier(() -> 0, () -> 0);
	}

	@Override
	public DriveAxesSupplier getGreenbergDriveAxes() { return new DriveAxesSupplier(() -> 0, () -> 0); }

	@Override
	public Optional getFirstController () {
		return Optional.empty();
	}

	@Override
	public Optional getSecondController () {
		return Optional.empty();
	}
}
