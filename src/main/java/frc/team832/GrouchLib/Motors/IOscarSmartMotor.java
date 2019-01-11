package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * Interface describing all methods a SmartMotor (CAN-based controller) must implement
 */
public interface IOscarSmartMotor extends IOscarSimpleMotor {

    /**
     * Sets the ControlMode of the controller.
     *
     * @param mode ControlMode to set.
     */
    void setMode(ControlMode mode);

    /**
     * Sets this motor controller to follow the output of another controller.
     *
     * @param masterMotorID CAN ID of the controller to follow.
     */
    void follow(int masterMotorID);

    /**
     * Sets this motor controller to follow the output of another controller.
     *
     * @param masterMotor IOscarSmartMotor to follow.
     */
    void follow(IOscarSmartMotor masterMotor);

    /**
     * Gets the current encoder position in native ticks.
     *
     * @return Position.
     */
    int getPosition();

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
     * Gets the CAN Id of the device.
     *
     * @return Device CAN ID.
     */
    int getDeviceID();

    int getBaseID();

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
    void setSensor(FeedbackDevice device);

    /**
     * Sets the allowed error for a closed-loop system to consider itself "stable".
     *
     * @param error Value of the allowable closed-loop error.
     */
    void setAllowableClosedLoopError(int error);

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

    int getSensorPosition();

    void setSensorPosition(int absolutePosition);

    double getClosedLoopTarget();

    boolean getForwardLimitSwitch();

    boolean getReverseLimitSwitch();

    int getClosedLoopError();

    int getPulseWidthPosition();

    void set_kF(int slot, double kF);

    void setPeakOutputForward(double percentOut);

    void setPeakOutputReverse(double percentOut);

    void setNominalOutputForward(double percentOut);

    void setNominalOutputReverse(double percentOut);
}
