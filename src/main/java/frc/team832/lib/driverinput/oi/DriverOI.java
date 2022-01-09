package frc.team832.lib.driverinput.oi;

public interface DriverOI {
	DriveAxesSupplier getTankDriveAxes();
	DriveAxesSupplier getArcadeDriveAxes();
	DriveAxesSupplier getGreenbergDriveAxes();
}
