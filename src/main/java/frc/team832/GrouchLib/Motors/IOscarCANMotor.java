package frc.team832.GrouchLib.Motors;

public interface IOscarCANMotor extends IOscarSimpleMotor {
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
     * Gets the CAN Id of the device.
     *
     * @return Device CAN ID.
     */
    int getDeviceID();

    int getBaseID();
}
