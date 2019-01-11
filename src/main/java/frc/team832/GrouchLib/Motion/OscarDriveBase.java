package frc.team832.GrouchLib.Motion;

public abstract class OscarDriveBase
//        extends MotorSafety
{

    public static final double kDefaultDeadband = 0.02;
    public static final double kDefaultMaxOutput = 1.0;

    protected double m_deadband = kDefaultDeadband;
    protected double m_maxOutput = kDefaultMaxOutput;

    public OscarDriveBase() {

//        setSafetyEnabled(true);
    }

    /**
     * Sets the deadband applied to the drive inputs (e.g., joystick values).
     *
     * <p>The default value is {@value #kDefaultDeadband}. Inputs smaller than the deadband are set to
     * 0.0 while inputs larger than the deadband are scaled from 0.0 to 1.0. See
     * {@link #applyDeadband}.
     *
     * @param deadband The deadband to set.
     */
    public void setDeadband(double deadband) {
        m_deadband = deadband;
    }

    /**
     * Configure the scaling factor for using drive methods with motor controllers in a mode other
     * than PercentVbus or to limit the maximum output.
     *
     * <p>The default value is {@value #kDefaultMaxOutput}.
     *
     * @param maxOutput Multiplied with the output percentage computed by the drive functions.
     */
    public void setMaxOutput(double maxOutput) {
        m_maxOutput = maxOutput;
    }

    //    @Override
    public abstract void stopMotor();

    /**
     * Limit motor values to the -1.0 to +1.0 range.
     */
    protected double limit(double value) {
        if (value > 1.0) {
            return 1.0;
        }
        if (value < -1.0) {
            return -1.0;
        }
        return value;
    }

    /**
     * Returns 0.0 if the given value is within the specified range around zero. The remaining range
     * between the deadband and 1.0 is scaled from 0.0 to 1.0.
     *
     * @param value    value to clip
     * @param deadband range around zero
     */
    protected double applyDeadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
            if (value > 0.0) {
                return (value - deadband) / (1.0 - deadband);
            } else {
                return (value + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }

    /**
     * Normalize all wheel speeds if the magnitude of any wheel is greater than 1.0.
     */
    protected void normalize(double[] wheelSpeeds) {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);
        for (int i = 1; i < wheelSpeeds.length; i++) {
            double temp = Math.abs(wheelSpeeds[i]);
            if (maxMagnitude < temp) {
                maxMagnitude = temp;
            }
        }
        if (maxMagnitude > 1.0) {
            for (int i = 0; i < wheelSpeeds.length; i++) {
                wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
            }
        }
    }
}
