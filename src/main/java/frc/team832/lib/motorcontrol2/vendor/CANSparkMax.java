package frc.team832.lib.motorcontrol2.vendor;

import com.revrobotics.CANSparkMaxLowLevel;
import frc.team832.lib.CANDevice;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol2.PowerManagedMC;
import frc.team832.lib.motorcontrol2.SmartMC;

import static com.revrobotics.CANSparkMax.*;

public class CANSparkMax extends PowerManagedMC<com.revrobotics.CANSparkMax> {

    private com.revrobotics.CANSparkMax _spark;

    public CANSparkMax(int canId, CANSparkMaxLowLevel.MotorType motorType) {
        _spark = new com.revrobotics.CANSparkMax(canId, motorType);

        boolean onBus = !_spark.getFirmwareString().equals("");
        CANDevice.addDevice(new CANDevice(canId, onBus, "SparkMax"));
    }

    @Override
    public void follow(SmartMC masterMC) {
        if (masterMC instanceof CANSparkMax) {
            _spark.follow((com.revrobotics.CANSparkMax)masterMC.getBaseController());
        } else {
            _spark.follow(ExternalFollower.kFollowerPhoenix, masterMC.getCANID());
        }
    }

    @Override
    public double getInputVoltage() {
        return _spark.getBusVoltage();
    }

    @Override
    public double getOutputVoltage() {
        return _spark.getAppliedOutput();
    }

    @Override
    public double getInputCurrent() {
        return _spark.getOutputCurrent(); // todo: any way to do this?
    }

    @Override
    public double getOutputCurrent() {
        return _spark.getOutputCurrent();
    }

    @Override
    public void setNeutralMode(NeutralMode mode) {
        _spark.setIdleMode(mode == NeutralMode.kBrake ?
                IdleMode.kBrake :
                IdleMode.kCoast);
    }

    @Override
    public int getCANID() {
        return _spark.getDeviceId();
    }

    @Override
    public void set(double power) {
        _spark.set(power);
    }

    @Override
    public double get() {
        return _spark.getAppliedOutput();
    }

    @Override
    public void stop() {
        _spark.set(0);
    }

    @Override
    public void setInverted(boolean inverted) {
        _spark.setInverted(inverted);
    }

    @Override
    public boolean getInverted() {
        return _spark.getInverted();
    }

    @Override
    public com.revrobotics.CANSparkMax getBaseController() {
        return _spark;
    }
}
