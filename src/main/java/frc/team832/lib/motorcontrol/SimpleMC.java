package frc.team832.lib.motorcontrol;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import frc.team832.lib.motors.Motor;

/** Interface for simple motor controllers
 * @param B Base motor controller type
 */
public interface SimpleMC<B> extends MotorController {

	/**
	 * Gets the motor attached to the controller.
	 * @return Motor object.
	 */
	Motor getMotor();

	/**
	 * Gets the base object of the controller.
	 * @return Base object.
	 */
	B getBaseController();

	/**
	 * Gets the current output voltage of the controller.
	 * @return Output in Volts.
	 */
  default double getOutputVoltage() {
    return RobotController.getBatteryVoltage() / get();
  }
}
