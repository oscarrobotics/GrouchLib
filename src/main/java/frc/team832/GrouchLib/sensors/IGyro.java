package frc.team832.GrouchLib.sensors;

public interface IGyro {

    /**
     * Initialize the gyro.
     *
     * @return boolean indicating whether initialization succeeded or not
     */
    public boolean init();

    public void calibrate();

    public void zero();

    public double getYaw();

    public double getPitch();

    public double getRoll();

}
