package frc.team832.lib.motorcontrol2;

import frc.team832.lib.motorcontrol.NeutralMode;

public interface SmartMC<B> extends SimpleMC<B> {

    void follow(SmartMC masterMC);

    /**
     * Gets the current input voltage of the controller.
     *
     * @return Input in Volts.
     */
    double getInputVoltage();

    /**
     * Gets the current output voltage of the controller.
     *
     * @return Output in Volts.
     */
    double getOutputVoltage();

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

    int getCANID();

    void wipeSettings();

    void limitInputCurrent(int currentLimit);

    double getSensorPosition();

    double getSensorVelocity();

    void setVelocity (double v);
}