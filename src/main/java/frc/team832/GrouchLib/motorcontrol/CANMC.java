package frc.team832.GrouchLib.motorcontrol;

public interface CANMC extends SimpleMC {
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
    void follow(CANMC masterMotor);

    /**
     * Gets the CAN Id of the device.
     *
     * @return Device CAN ID.
     */
    int getDeviceID();

    int getBaseID();
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

    void resetSettings();
}
