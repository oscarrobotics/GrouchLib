package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * Interface describing all methods a SmartMotor (CAN-based controller) must implement
 */
public interface SmartMotor extends SimpleMotor, PIDMotor {

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

    /**
     * Sets the closed-loop ramp rate of throttle output.
     *
     * @param secondsFromNeutralToFull Minimum desired time to go from neutral to full throttle. A value of '0' will disable the ramp.
     */
    void setClosedLoopRamp(double secondsFromNeutralToFull);

    /**
     * Sets the open-loop ramp rate of throttle output.
     *
     * @param secondsFromNeutralToFull Minimum desired time to go from neutral to full throttle. A value of '0' will disable the ramp.
     */
    void setOpenLoopRamp(double secondsFromNeutralToFull);

    boolean getForwardLimitSwitch();

    boolean getReverseLimitSwitch();

    void setPeakOutputForward(double percentOut);

    void setPeakOutputReverse(double percentOut);

    void setNominalOutputForward(double percentOut);

    void setNominalOutputReverse(double percentOut);

    void setForwardSoftLimit(int limit);

    void setReverseSoftLimit(int limit);

    void setVelocity(double rpmVal);

    void setPosition(double posVal);
}
