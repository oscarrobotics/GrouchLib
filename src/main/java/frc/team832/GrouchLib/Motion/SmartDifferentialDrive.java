package frc.team832.GrouchLib.Motion;

import frc.team832.GrouchLib.Motors.SmartMotor;

public class SmartDifferentialDrive extends DriveBase {
    public static final double kDefaultQuickStopThreshold = 0.2;
    public static final double kDefaultQuickStopAlpha = 0.1;

    private int _maxRpm;

    private SmartMotor _leftMotor;
    private SmartMotor _rightMotor;

    private double m_quickStopThreshold = kDefaultQuickStopThreshold;
    private double m_quickStopAlpha = kDefaultQuickStopAlpha;
    private double m_quickStopAccumulator = 0.0;

    public enum LoopMode {
        PERCENTAGE,
        VELOCITY,
        POSITION
    }

    public SmartDifferentialDrive(SmartMotor leftMotor, SmartMotor rightMotor, int maxRpm) {
        _leftMotor = leftMotor;
        _rightMotor = rightMotor;
        _maxRpm = maxRpm;
    }

    /**
     * Arcade drive method for differential drive platform.
     * The calculated values will be squared to decrease sensitivity at low speeds.
     *
     * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                  positive.
     */
    @SuppressWarnings("ParameterName")
    public void arcadeDrive(double xSpeed, double zRotation, LoopMode loopMode) {
        arcadeDrive(xSpeed, zRotation, false, loopMode);
    }

    /**
     * Arcade drive method for differential drive platform.
     *
     * @param xSpeed        The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param zRotation     The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                      positive.
     * @param squaredInputs If set, decreases the input sensitivity at low speeds.
     */
    @SuppressWarnings("ParameterName")
    public void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs, LoopMode loopMode) {

        xSpeed = limit(xSpeed);
        xSpeed = applyDeadband(xSpeed, m_deadband);

        zRotation = limit(zRotation);
        zRotation = applyDeadband(zRotation, m_deadband);

        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        if (squaredInputs) {
            xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
            zRotation = Math.copySign(zRotation * zRotation, zRotation);
        }

        double leftMotorOutput;
        double rightMotorOutput;

        double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

        if (xSpeed >= 0.0) {
            // First quadrant, else second quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            } else {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            } else {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            }
        }

        double leftPowerOut = limit(leftMotorOutput) * _maxOutput;
        double rightPowerOut = -(limit(rightMotorOutput) * _maxOutput);

        switch (loopMode) {
            case PERCENTAGE:
                _leftMotor.set(leftPowerOut);
                _rightMotor.set(-rightPowerOut);
                break;
            case VELOCITY:
                _leftMotor.setVelocity(leftPowerOut * _maxRpm);
                _rightMotor.setVelocity(rightPowerOut * _maxRpm);
                break;
            case POSITION:
                break;
        }
    }

    /**
     * Curvature drive method for differential drive platform.
     *
     * <p>The rotation argument controls the curvature of the robot's path rather than its rate of
     * heading change. This makes the robot more controllable at high speeds. Also handles the
     * robot's quick turn functionality - "quick turn" overrides constant-curvature turning for
     * turn-in-place maneuvers.
     *
     * @param xSpeed      The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param zRotation   The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                    positive.
     * @param isQuickTurn If set, overrides constant-curvature turning for
     *                    turn-in-place maneuvers.
     */
    @SuppressWarnings("ParameterName")
    public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn, LoopMode loopMode) {
        xSpeed = limit(xSpeed);
        xSpeed = applyDeadband(xSpeed, m_deadband);

        zRotation = limit(zRotation);
        zRotation = applyDeadband(zRotation, m_deadband);

        double angularPower;
        boolean overPower;

        if (isQuickTurn) {
            if (Math.abs(xSpeed) < m_quickStopThreshold) {
                m_quickStopAccumulator = (1 - m_quickStopAlpha) * m_quickStopAccumulator
                        + m_quickStopAlpha * limit(zRotation) * 2;
            }
            overPower = true;
            angularPower = zRotation;
        } else {
            overPower = false;
            angularPower = Math.abs(xSpeed) * zRotation - m_quickStopAccumulator;

            if (m_quickStopAccumulator > 1) {
                m_quickStopAccumulator -= 1;
            } else if (m_quickStopAccumulator < -1) {
                m_quickStopAccumulator += 1;
            } else {
                m_quickStopAccumulator = 0.0;
            }
        }

        double leftMotorOutput = xSpeed + angularPower;
        double rightMotorOutput = xSpeed - angularPower;

        // If rotation is overpowered, reduce both outputs to within acceptable range
        if (overPower) {
            if (leftMotorOutput > 1.0) {
                rightMotorOutput -= leftMotorOutput - 1.0;
                leftMotorOutput = 1.0;
            } else if (rightMotorOutput > 1.0) {
                leftMotorOutput -= rightMotorOutput - 1.0;
                rightMotorOutput = 1.0;
            } else if (leftMotorOutput < -1.0) {
                rightMotorOutput -= leftMotorOutput + 1.0;
                leftMotorOutput = -1.0;
            } else if (rightMotorOutput < -1.0) {
                leftMotorOutput -= rightMotorOutput + 1.0;
                rightMotorOutput = -1.0;
            }
        }

        // Normalize the wheel speeds
        double maxMagnitude = Math.max(Math.abs(leftMotorOutput), Math.abs(rightMotorOutput));
        if (maxMagnitude > 1.0) {
            leftMotorOutput /= maxMagnitude;
            rightMotorOutput /= maxMagnitude;
        }

        rightMotorOutput *= -1; // invert right side

        switch (loopMode) {
            case PERCENTAGE:
                _leftMotor.set(leftMotorOutput);
                _rightMotor.set(rightMotorOutput);
                break;
            case VELOCITY:
                _leftMotor.setVelocity(leftMotorOutput * _maxRpm);
                _rightMotor.setVelocity(rightMotorOutput * _maxRpm);

                System.out.println("left tar; " + leftMotorOutput * _maxRpm);
                System.out.println("right tar; " + rightMotorOutput * _maxRpm);

                System.out.println("left; " + _leftMotor.getSensorVelocity());
                System.out.println("right; " + _rightMotor.getSensorVelocity());
                break;
            case POSITION:
                break;
        }
    }

    /**
     * Tank drive method for differential drive platform.
     * The calculated values will be squared to decrease sensitivity at low speeds.
     *
     * @param leftSpeed  The robot's left side speed along the X axis [-1.0..1.0]. Forward is
     *                   positive.
     * @param rightSpeed The robot's right side speed along the X axis [-1.0..1.0]. Forward is
     *                   positive.
     */
    public void tankDrive(double leftSpeed, double rightSpeed, LoopMode loopMode) {
        tankDrive(leftSpeed, rightSpeed, false, loopMode);
    }

    /**
     * Tank drive method for differential drive platform.
     *
     * @param leftSpeed     The robot left side's speed along the X axis [-1.0..1.0]. Forward is
     *                      positive.
     * @param rightSpeed    The robot right side's speed along the X axis [-1.0..1.0]. Forward is
     *                      positive.
     * @param squaredInputs If set, decreases the input sensitivity at low speeds.
     */
    public void tankDrive(double leftSpeed, double rightSpeed, boolean squaredInputs, LoopMode loopMode) {
        leftSpeed = limit(leftSpeed);
        leftSpeed = applyDeadband(leftSpeed, m_deadband);

        rightSpeed = limit(rightSpeed);
        rightSpeed = applyDeadband(rightSpeed, m_deadband);

        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        if (squaredInputs) {
            leftSpeed = Math.copySign(leftSpeed * leftSpeed, leftSpeed);
            rightSpeed = Math.copySign(rightSpeed * rightSpeed, rightSpeed);
        }

        double leftPowerOut = leftSpeed * _maxOutput;
        double rightPowerOut = -rightSpeed * _maxOutput;

        switch (loopMode) {
            case PERCENTAGE:
                _leftMotor.set(leftPowerOut);
                _rightMotor.set(rightPowerOut);
                break;
            case VELOCITY:
                _leftMotor.setVelocity(leftPowerOut * _maxRpm);
                _rightMotor.setVelocity(rightPowerOut * _maxRpm);
                break;
            case POSITION:
                break;
        }
    }

    /**
     * Sets the QuickStop speed threshold in curvature drive.
     *
     * <p>QuickStop compensates for the robot's moment of inertia when stopping after a QuickTurn.
     *
     * <p>While QuickTurn is enabled, the QuickStop accumulator takes on the rotation rate value
     * outputted by the low-pass filter when the robot's speed along the X axis is below the
     * threshold. When QuickTurn is disabled, the accumulator's value is applied against the computed
     * angular power request to slow the robot's rotation.
     *
     * @param threshold X speed below which quick stop accumulator will receive rotation rate values
     *                  [0..1.0].
     */
    public void setQuickStopThreshold(double threshold) {
        m_quickStopThreshold = threshold;
    }

    /**
     * Sets the low-pass filter gain for QuickStop in curvature drive.
     *
     * <p>The low-pass filter filters incoming rotation rate commands to smooth out high frequency
     * changes.
     *
     * @param alpha Low-pass filter gain [0.0..2.0]. Smaller values result in slower output changes.
     *              Values between 1.0 and 2.0 result in output oscillation. Values below 0.0 and
     *              above 2.0 are unstable.
     */
    public void setQuickStopAlpha(double alpha) {
        m_quickStopAlpha = alpha;
    }

    @Override
    public void stopMotor() {
        _leftMotor.stopMotor();
        _rightMotor.stopMotor();
    }

    @Override
    public double getLeftOutput() {
        return _leftMotor.get();
    }

    @Override
    public double getRightOutput() {
        return _rightMotor.get();
    }

    public double getLeftPosition() { return _leftMotor.getSensorPosition(); }
    public double getRightPosition() { return _rightMotor.getSensorPosition(); }

    public double getLeftVelocity() { return _leftMotor.getSensorVelocity(); }
    public double getRightVelocity() { return _rightMotor.getSensorVelocity(); }

    public void setPIDF(double kP, double kI, double kD, double kF){
        setP(kP);
        setI(kI);
        setD(kD);
        setF(kF);
    }
    
    public void setP(double kP) {
        _leftMotor.setkP(kP);
        _rightMotor.setkP(kP);
    }
    
    public void setI(double kI) {
        _leftMotor.setkI(kI);
        _rightMotor.setkI(kI);
    }

    public void setD(double kD) {
        _leftMotor.setkD(kD);
        _rightMotor.setkD(kD);
    }

    public void setF(double kF) {
        _leftMotor.setkF(kF);
        _rightMotor.setkF(kF);
    }
}
