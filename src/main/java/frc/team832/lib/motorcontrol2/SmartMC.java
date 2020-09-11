package frc.team832.lib.motorcontrol2;

import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motion.ClosedLoopConfig;
import frc.team832.lib.control.can.OscarCANDevice;

@SuppressWarnings("rawtypes")
public interface SmartMC<B> extends SimpleMC<B>, OscarCANDevice {

    void follow(SmartMC masterMC);

    /**
     * Gets the current input voltage of the controller.
     *
     * @return Input in Volts.
     */
    double getInputVoltage();


    /**
     * Gets the input current of the controller.
     *
     * @return Input in Amps.
     */
    double getInputCurrent();

    /**
     * Gets the current output current of the controller.
     *
     * @return Output in Amps.
     */
    double getOutputCurrent();

    /**
     * Sets the NeutralMode of the motor. (Brake or Coast)
     *
     * @param mode NeutralMode to set.
     */
    void setNeutralMode(NeutralMode mode);

    void wipeSettings();

    void limitInputCurrent(int currentLimit);

    double getSensorPosition();

    double getSensorVelocity();

    void rezeroSensor();

    void setTargetPosition(double target);

    void setTargetPosition(double target, double arbFF);

    void setTargetVelocity(double target);

    void setTargetVelocity(double target, double arbFF);

    void setSensorPhase(boolean phase);

    void setPIDF(ClosedLoopConfig closedLoopConfig);
}