package frc.team832.GrouchLib.Sensors;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SerialPort;

public class OscarNavX implements IOscarIMU {

    private AHRS _ahrs;
    private I2C.Port i2cPort;
    private SerialPort.Port serialPort;
    private byte _updateRate = 60;

    public OscarNavX(I2C.Port port) {
        i2cPort = port;
    }

    public OscarNavX(I2C.Port port, byte updateRate) {
        _updateRate = updateRate;
        i2cPort = port;
    }

    public OscarNavX(SerialPort.Port port) {
        serialPort = port;
    }

    public OscarNavX(SerialPort.Port port, byte updateRate) {
        _updateRate = updateRate;
        serialPort = port;
    }

    @Override
    public double getXAccel() {
        return _ahrs.getRawAccelX();
    }

    @Override
    public double getYAccel() {
        return _ahrs.getRawAccelY();
    }

    @Override
    public double getZAccel() {
        return _ahrs.getRawAccelY();
    }

    @Override
    public double getXDisplacement() {
        return _ahrs.getDisplacementX();
    }

    @Override
    public double getYDisplacement() {
        return _ahrs.getDisplacementY();
    }

    @Override
    public double getZDisplacement() {
        return _ahrs.getDisplacementZ();
    }

    @Override
    public double getXMag() {
        return _ahrs.getRawMagX();
    }

    @Override
    public double getYMag() {
        return _ahrs.getRawMagY();
    }

    @Override
    public double getZMag() {
        return _ahrs.getRawMagZ();
    }

    @Override
    public boolean init() {
        try {
            if (i2cPort == null) {
                _ahrs = new AHRS(serialPort, AHRS.SerialDataType.kProcessedData, _updateRate);
            } else {
                _ahrs = new AHRS(i2cPort, _updateRate);
            }
        } catch (Exception ex) {
            System.out.println("Failed to init gyro:\n");
            System.out.println(ex.getMessage());
            return false;
        }

        _ahrs.reset();
        _ahrs.resetDisplacement();
        return true;
    }

    @Override
    public void calibrate() {
        return;
    }

    @Override
    public void zero() {
        _ahrs.zeroYaw();
    }

    @Override
    public double getYaw() {
        return _ahrs.getYaw();
    }

    @Override
    public double getPitch() {
        return _ahrs.getPitch();
    }

    @Override
    public double getRoll() {
        return _ahrs.getRoll();
    }
}
