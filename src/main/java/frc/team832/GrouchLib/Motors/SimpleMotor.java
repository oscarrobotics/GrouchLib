package frc.team832.GrouchLib.Motors;

public interface SimpleMotor {

    /**
     * Common interface for setting the speed of a speed controller.
     *
     * @param speed The speed to set. Value should be between -1.0 and 1.0.
     */
    void set(double speed);

    /**
     * Common interface for getting the current set speed of a speed controller.
     *
     * @return The current set speed. Value is between -1.0 and 1.0.
     */
    double get();

    /**
     * Common interface for returning if a speed controller is in the inverted get or not.
     *
     * @return isInverted The get of the inversion true is inverted.
     */
    boolean getInverted();

    /**
     * Common interface for inverting direction of a speed controller.
     *
     * @param isInverted The get of inversion true is inverted.
     */
    void setInverted(boolean isInverted);

    void setNeutralMode(NeutralMode mode);

    /**
     * Disable the speed controller.
     */
    void disable();

    /**
     * Stops motor movement. Motor can be moved again by calling set without having to re-enabled the
     * motor.
     */
    void stopMotor();
}
