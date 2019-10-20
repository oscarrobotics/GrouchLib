package frc.team832.lib.driverstation.controllers;

import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class Xbox360Controller extends GenericHID {
   /**
    * Represents a digital button on an XboxController.
    */
   private enum Button {
     kBumperLeft(5),
     kBumperRight(6),
     kStickLeft(9),
     kStickRight(10),
     kA(1),
     kB(2),
     kX(3),
     kY(4),
     kBack(7),
     kStart(8);

     public final int value;

     Button(int value) {
       this.value = value;
     }
   }

   public final JoystickButton leftBumper = new JoystickButton(this, Button.kBumperLeft.value);
   public final JoystickButton rightBumper = new JoystickButton(this, Button.kBumperRight.value);
   public final JoystickButton leftStickPress = new JoystickButton(this, Button.kStickLeft.value);
   public final JoystickButton rightStickPress = new JoystickButton(this, Button.kStickRight.value);
   public final JoystickButton aButton = new JoystickButton(this, Button.kA.value);
   public final JoystickButton bButton = new JoystickButton(this, Button.kB.value);
   public final JoystickButton xButton = new JoystickButton(this, Button.kX.value);
   public final JoystickButton yButton = new JoystickButton(this, Button.kY.value);
   public final JoystickButton backButton = new JoystickButton(this, Button.kBack.value);
   public final JoystickButton startButton = new JoystickButton(this, Button.kStart.value);

    /**
   * Construct an instance of a joystick. The joystick index is the USB port on the drivers
   * station.
   *
   * @param port The port on the Driver Station that the joystick is plugged into.
   */
  public Xbox360Controller(final int port) {
    super(port);

    HAL.report(tResourceType.kResourceType_XboxController, port);
  }

  /**
   * Get the X axis value of the controller.
   *
   * @param hand Side of controller whose value should be returned.
   * @return The X axis value of the controller.
   */
  @Override
  public double getX(Hand hand) {
    return hand.equals(Hand.kLeft) ? getRawAxis(0) : getRawAxis(4);
  }

  /**
   * Get the Y axis value of the controller.
   *
   * @param hand Side of controller whose value should be returned.
   * @return The Y axis value of the controller.
   */
  @Override
  public double getY(Hand hand) {
    return hand.equals(Hand.kLeft) ? getRawAxis(1) : getRawAxis(5);
  }

  /**
   * Get the trigger axis value of the controller.
   *
   * @param hand Side of controller whose value should be returned.
   * @return The trigger axis value of the controller.
   */
  public double getTriggerAxis(Hand hand) {
    return hand.equals(Hand.kLeft) ? getRawAxis(2) : getRawAxis(3);
  }
}