package edu.wpi.first.wpilibj2.command;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static edu.wpi.first.wpilibj.util.ErrorMessages.requireNonNullParam;

/**
 * A command that uses a RAMSETE controller ({@link RamseteController}) to follow a trajectory
 * {@link Trajectory} with a differential drive.
 *
 * <p>The command handles trajectory-following, PID calculations, and feedforwards internally.  This
 * is intended to be a more-or-less "complete solution" that can be used by teams without a great
 * deal of controls expertise.
 *
 * <p>Advanced teams seeking more flexibility (for example, those who wish to use the onboard
 * PID functionality of a "smart" motor controller) may use the secondary constructor that omits
 * the PID and feedforward functionality, returning only the raw wheel speeds from the RAMSETE
 * controller.
 */
@SuppressWarnings("PMD.TooManyFields")
public class OscarRamseteCommand extends CommandBase {
  private final Timer m_timer = new Timer();
  private final boolean m_usePID;
  private final Trajectory m_trajectory;
  private final Supplier<Pose2d> m_pose;
  private final RamseteController m_follower;
  private final SimpleMotorFeedforward m_leftFeedForward;
  private final SimpleMotorFeedforward m_rightFeedForward;
  private final DifferentialDriveKinematics m_kinematics;
  private final Supplier<DifferentialDriveWheelSpeeds> m_speeds;
  private final PIDController m_leftController;
  private final PIDController m_rightController;
  private final BiConsumer<Double, Double> m_output;
  private DifferentialDriveWheelSpeeds m_prevSpeeds;
  private double m_prevTime;

  /**
   * Constructs a new RamseteCommand that, when executed, will follow the provided trajectory.
   * PID control and feedforward are handled internally, and outputs are scaled -12 to 12
   * representing units of volts.
   *
   * <p>Note: The controller will *not* set the outputVolts to zero upon completion of the path -
   * this
   * is left to the user, since it is not appropriate for paths with nonstationary endstates.
   *
   * @param trajectory        The trajectory to follow.
   * @param pose              A function that supplies the robot pose - use one of
   *                          the odometry classes to provide this.
   * @param controller        The RAMSETE controller used to follow the trajectory.
   * @param leftFeedForward   The feedforward to use for the left drive.
   * @param rightFeedForward  The feedforward to use for the right drive.
   * @param kinematics        The kinematics for the robot drivetrain.
   * @param wheelSpeeds       A function that supplies the speeds of the left and
   *                          right sides of the robot drive.
   * @param leftController    The PIDController for the left side of the robot drive.
   * @param rightController   The PIDController for the right side of the robot drive.
   * @param outputVolts       A function that consumes the computed left and right
   *                          outputs (in volts) for the robot drive.
   * @param requirements      The subsystems to require.
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public OscarRamseteCommand(Trajectory trajectory,
                             Supplier<Pose2d> pose,
                             RamseteController controller,
                             SimpleMotorFeedforward leftFeedForward,
                             SimpleMotorFeedforward rightFeedForward,
                             DifferentialDriveKinematics kinematics,
                             Supplier<DifferentialDriveWheelSpeeds> wheelSpeeds,
                             PIDController leftController,
                             PIDController rightController,
                             BiConsumer<Double, Double> outputVolts,
                             Subsystem... requirements) {
    m_trajectory = requireNonNullParam(trajectory, "trajectory", "RamseteCommand");
    m_pose = requireNonNullParam(pose, "pose", "RamseteCommand");
    m_follower = requireNonNullParam(controller, "controller", "RamseteCommand");
    m_leftFeedForward = leftFeedForward;
    m_rightFeedForward = rightFeedForward;
    m_kinematics = requireNonNullParam(kinematics, "kinematics", "RamseteCommand");
    m_speeds = requireNonNullParam(wheelSpeeds, "wheelSpeeds", "RamseteCommand");
    m_leftController = requireNonNullParam(leftController, "leftController", "RamseteCommand");
    m_rightController = requireNonNullParam(rightController, "rightController", "RamseteCommand");
    m_output = requireNonNullParam(outputVolts, "outputVolts", "RamseteCommand");

    m_usePID = true;

    addRequirements(requirements);
  }

  /**
   * Constructs a new RamseteCommand that, when executed, will follow the provided trajectory.
   * Performs no PID control and calculates no feedforwards; outputs are the raw wheel speeds
   * from the RAMSETE controller, and will need to be converted into a usable form by the user.
   *
   * @param trajectory            The trajectory to follow.
   * @param pose                  A function that supplies the robot pose - use one of
   *                              the odometry classes to provide this.
   * @param follower              The RAMSETE follower used to follow the trajectory.
   * @param kinematics            The kinematics for the robot drivetrain.
   * @param outputMetersPerSecond A function that consumes the computed left and right
   *                              wheel speeds.
   * @param requirements          The subsystems to require.
   */
  public OscarRamseteCommand(Trajectory trajectory,
                             Supplier<Pose2d> pose,
                             RamseteController follower,
                             DifferentialDriveKinematics kinematics,
                             BiConsumer<Double, Double> outputMetersPerSecond,
                             Subsystem... requirements) {
    m_trajectory = requireNonNullParam(trajectory, "trajectory", "RamseteCommand");
    m_pose = requireNonNullParam(pose, "pose", "RamseteCommand");
    m_follower = requireNonNullParam(follower, "follower", "RamseteCommand");
    m_kinematics = requireNonNullParam(kinematics, "kinematics", "RamseteCommand");
    m_output = requireNonNullParam(outputMetersPerSecond, "output", "RamseteCommand");

    m_leftFeedForward = null;
    m_rightFeedForward = null;
    m_speeds = null;
    m_leftController = null;
    m_rightController = null;

    m_usePID = false;

    addRequirements(requirements);
  }

  @Override
  public void initialize() {
    m_prevTime = 0;
    var initialState = m_trajectory.sample(0);
    m_prevSpeeds = m_kinematics.toWheelSpeeds(
        new ChassisSpeeds(initialState.velocityMetersPerSecond,
            0,
            initialState.curvatureRadPerMeter
                * initialState.velocityMetersPerSecond));
    m_timer.reset();
    m_timer.start();
    if (m_usePID) {
      m_leftController.reset();
      m_rightController.reset();
    }
  }

  @Override
  public void execute() {
    double curTime = m_timer.get();
    double dt = curTime - m_prevTime;

    var targetWheelSpeeds = m_kinematics.toWheelSpeeds(
        m_follower.calculate(m_pose.get(), m_trajectory.sample(curTime)));

    var leftSpeedSetpoint = targetWheelSpeeds.leftMetersPerSecond;
    var rightSpeedSetpoint = targetWheelSpeeds.rightMetersPerSecond;

    SmartDashboard.putNumber("OscarRamsete/WheelSpeeds/LeftTarget", leftSpeedSetpoint);
    SmartDashboard.putNumber("OscarRamsete/WheelSpeeds/RightTarget", rightSpeedSetpoint);

    double leftOutput;
    double rightOutput;

    if (m_usePID) {
      double leftFeedforward =
          m_leftFeedForward.calculate(leftSpeedSetpoint,
              (leftSpeedSetpoint - m_prevSpeeds.leftMetersPerSecond) / dt);

      if (RobotBase.isSimulation()) {
        leftFeedforward -= m_leftFeedForward.ks * Math.signum(leftSpeedSetpoint);
      }

      SmartDashboard.putNumber("OscarRamsete/FF/Left", leftFeedforward);

      double rightFeedforward =
          m_rightFeedForward.calculate(rightSpeedSetpoint,
              (rightSpeedSetpoint - m_prevSpeeds.rightMetersPerSecond) / dt);

      if (RobotBase.isSimulation()) {
        rightFeedforward -= m_rightFeedForward.ks * Math.signum(rightSpeedSetpoint);
      }

      SmartDashboard.putNumber("OscarRamsete/FF/Right", rightFeedforward);

      double leftRealSpeed = m_speeds.get().leftMetersPerSecond;
      double rightRealSpeed = m_speeds.get().rightMetersPerSecond;

      SmartDashboard.putNumber("OscarRamsete/WheelSpeeds/LeftReal", leftRealSpeed);
      SmartDashboard.putNumber("OscarRamsete/WheelSpeeds/RightReal", rightRealSpeed);

      leftOutput = leftFeedforward
          + m_leftController.calculate(m_speeds.get().leftMetersPerSecond,
          leftSpeedSetpoint);

      rightOutput = rightFeedforward
          + m_rightController.calculate(m_speeds.get().rightMetersPerSecond,
          rightSpeedSetpoint);
    } else {
      leftOutput = leftSpeedSetpoint;
      rightOutput = rightSpeedSetpoint;
    }

    m_output.accept(leftOutput, rightOutput);

    m_prevTime = curTime;
    m_prevSpeeds = targetWheelSpeeds;
  }

  @Override
  public void end(boolean interrupted) {
    m_timer.stop();

    if (interrupted) {
      m_output.accept(0.0, 0.0);
    }
  }

  @Override
  public boolean isFinished() {
    return m_timer.advanceIfElapsed(m_trajectory.getTotalTimeSeconds());
  }
}
