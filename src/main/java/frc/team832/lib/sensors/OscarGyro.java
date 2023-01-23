package frc.team832.lib.sensors;

import edu.wpi.first.wpilibj.interfaces.Gyro;

public interface OscarGyro<B> extends Gyro {
    double getPitch();
    double getYaw();
    double getRoll(); 
}
