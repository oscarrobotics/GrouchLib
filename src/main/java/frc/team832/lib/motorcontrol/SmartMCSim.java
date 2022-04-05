package frc.team832.lib.motorcontrol;

public interface SmartMCSim {
    /**
     * @param voltage Bus voltage in volts.
     */
    void setBusVoltage(double voltage);

    /**
     * @return Output voltage in volts.
     */
    double getOutputVoltage();

    /**
     * @param position Position in rotations (360deg = 1 rotation).
     */
    void setSensorPosition(double position);

    /**
     * @param velocity Velocity in RPM.
     */
    void setSensorVelocity(double velocity);
}
