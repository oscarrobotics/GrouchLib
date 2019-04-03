package frc.team832.GrouchLib.Sensors;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.awt.*;

public class NavXMicro implements IMU {

    private AHRS _ahrs;
    private NavXPort _port;
    private byte _updateRate = 60;

    public NavXMicro(NavXPort port) {
        _port = port;
    }

    public NavXMicro(NavXPort port, byte updateRate) {
        _port = port;
        _updateRate = updateRate;
    }

    public void pushData(){
        SmartDashboard.putNumber("Yaw", getYeet());
        SmartDashboard.putNumber("Pitch", getPitch());
        SmartDashboard.putNumber("Roll", getRoll());
    }

    public double getYeet() {
        return _ahrs.getFusedHeading();
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
            switch (_port) {
                case I2C_onboard:
                    _ahrs = new AHRS(I2C.Port.kOnboard, _updateRate);
                    break;
                case I2C_mxp:
                    _ahrs = new AHRS(I2C.Port.kMXP, _updateRate);
                    break;
                case SPI_onboard:
                    _ahrs = new AHRS(SPI.Port.kOnboardCS0, _updateRate);
                    break;
                case SPI_mxp:
                    _ahrs = new AHRS(SPI.Port.kMXP, _updateRate);
                    break;
                case USB_onboard:
                    _ahrs = new AHRS(SerialPort.Port.kUSB, AHRS.SerialDataType.kProcessedData, _updateRate);
                    break;
                case Serial_mxp:
                    _ahrs = new AHRS(SerialPort.Port.kMXP, AHRS.SerialDataType.kProcessedData, _updateRate);
                    break;
            }

            _ahrs.reset();
            _ahrs.resetDisplacement();
            return true;
        } catch (Exception ex) {
            System.out.println(String.format("Failed to init NavX on port %s:\n", _port.toString()));
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public void calibrate() {
        // NavX auto-calibrates
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

    public enum NavXPort {
        I2C_onboard,
        I2C_mxp,
        SPI_onboard,
        SPI_mxp,
        USB_onboard,
        Serial_mxp
    }
}
