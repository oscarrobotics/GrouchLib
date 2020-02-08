package frc.team832.lib.drive;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import frc.team832.lib.motors.WheeledPowerTrain;

public class ClosedLoopDT extends OpenLoopDT {
    public final PIDController leftController;
    public final PIDController rightController;

    public ClosedLoopDT(SimpleMotorFeedforward leftFeedforward, SimpleMotorFeedforward rightFeedforward,
                        PIDController leftController, PIDController rightController, WheeledPowerTrain powerTrain) {
        super(leftFeedforward, rightFeedforward, powerTrain);
        this.leftController = leftController;
        this.rightController = rightController;
    }
}
