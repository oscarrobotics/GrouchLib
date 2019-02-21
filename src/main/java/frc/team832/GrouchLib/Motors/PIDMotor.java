package frc.team832.GrouchLib.Motors;

public interface PIDMotor {

	/**
	 * Gets the current sensor position in it's native unit.
	 *
	 * @return Position.
	 */
	double getSensorPosition();

	double getSensorVelocity();

	void setSensorPosition(int absolutePosition);

	void resetSensor();

	void setkP(double kP);

	void setkI(double kI);

	void setkD(double kD);

	void setkF(double kF);

	void setkP(double kP, int slotID);

	void setkI(double kI, int slotID);

	void setkD(double kD, int slotID);

	void setkF(double kF, int slotID);

	/**
	 * Sets the allowed error for a closed-loop system to consider itself "stable".
	 *
	 * @param error Value of the allowable closed-loop error.
	 */
	void setAllowableClosedLoopError(int error);

	int getAllowableClosedLoopError();

	int getClosedLoopError();

	double getTargetPosition();
}