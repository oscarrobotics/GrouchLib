package frc.team832.lib.sensors;

public interface IMU extends IGyro {

    double getXAccel();

    double getYAccel();

    double getZAccel();

    double getXDisplacement();

    double getYDisplacement();

    double getZDisplacement();

    double getXMag();

    double getYMag();

    double getZMag();
}
