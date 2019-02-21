package frc.team832.GrouchLib.Sensors;

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