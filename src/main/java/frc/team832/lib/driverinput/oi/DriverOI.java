package frc.team832.lib.driverinput.oi;

import java.util.Optional;

public interface DriverOI<A, B> {
    DriveAxesSupplier getTankDriveAxes();
    DriveAxesSupplier getArcadeDriveAxes();

    Optional<A> getFirstController();
    Optional<B> getSecondController();
}
