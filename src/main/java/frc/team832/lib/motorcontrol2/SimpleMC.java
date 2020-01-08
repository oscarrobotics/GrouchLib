package frc.team832.lib.motorcontrol2;

public interface SimpleMC<B> {

    /**
     *
     * @param power setpoint from -1.0 to 1.0
     */
    void set(double power);

    /**
     *
     * @return setpoint
     */
    double get();

    void stop();

    void setInverted(boolean inverted);

    boolean getInverted();

    B getBaseController();

	void stopMotor();
}
