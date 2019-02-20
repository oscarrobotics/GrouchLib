package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * Interface describing all methods a SmartMotor (CAN-based controller) must implement
 */
public interface ISmartMotor extends ISimpleMotor {

    /**
     * Gets the current sensor position in it's native unit.
     *
     * @return Position.
     */
    int getCurrentPosition();

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
     * Sets the phase of the sensor on this IOscarSmartMotor.
     *
     * @param phase Phase to set. (true = positive, false = negative)
     */
    void setSensorPhase(boolean phase);

    /**
     * Sets the feedback sensor type for any "smart" actions
     *
     * @param device FeedbackDevice to set.
     */
    void setSensorType(FeedbackDevice device);

    /**
     * Sets the allowed error for a closed-loop system to consider itself "stable".
     *
     * @param error Value of the allowable closed-loop error.
     */
    void setAllowableClosedLoopError(int error);

    int getAllowableClosedLoopError();

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

    int getSensorVelocity();

    int getSensorPosition();

    void setSensorPosition(int absolutePosition);

    double getTargetPosition();

    boolean getForwardLimitSwitch();

    boolean getReverseLimitSwitch();

    int getClosedLoopError();

    int getPulseWidthPosition();

    void set_kF(int slot, double kF);

    void setPeakOutputForward(double percentOut);

    void setPeakOutputReverse(double percentOut);

    void setNominalOutputForward(double percentOut);

    void setNominalOutputReverse(double percentOut);

    void setkP(double kP);

    void setkI(double kI);

    void setkD(double kD);

    void setkF(double kF);

    void setkP(double kP, int slotID);

    void setkI(double kI, int slotID);

    void setkD(double kD, int slotID);

    void setkF(double kF, int slotID);

    void setUpperLimit(int limit);

    void setLowerLimit(int limit);

    void resetSensor();

    void setVelocity(double rpmVal);

    void setPosition(double posVal);
}
