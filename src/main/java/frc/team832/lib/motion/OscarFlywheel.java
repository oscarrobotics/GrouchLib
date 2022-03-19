package frc.team832.lib.motion;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import frc.team832.lib.driverstation.dashboard.DashboardManager;
import frc.team832.lib.motorcontrol.SmartMC;
import frc.team832.lib.motors.WheeledPowerTrain;

public class OscarFlywheel {
	private static int INSTANCE_COUNT = 0;
	private static String GetInstanceName() {
		return OscarFlywheel.class.getSimpleName() + INSTANCE_COUNT;
	}
	
	private final SmartMC<?, ?> m_motor;
	private final WheeledPowerTrain m_powertrain;
	private final SimpleMotorFeedforward m_feedforward;
	private final PIDController m_pidController;
	private final FlywheelSim m_sim;

	private boolean m_runClosedLoop = false;

	private double m_targetVelocityRpm;
	private double m_ffEffortVolts;
	private double m_pEffortVolts;

	// Motor data
	private final NetworkTableEntry nte_motorDutyCycle, nte_motorOutputVoltage, nte_motorInputCurrent;

	// Control data
	private final NetworkTableEntry nte_targetVelocityRpm;

	// Sensor data
	private final NetworkTableEntry nte_encVelocityRpm, nte_encSurfaceSpeedMps;

	// Control loop data
	private final NetworkTableEntry nte_ffEffortVolts, nte_pEffortVolts;

	public OscarFlywheel(
		SmartMC<?, ?> motor, WheeledPowerTrain powertrain,
		SimpleMotorFeedforward feedforward, 
		double kP, double moiKgM2
	) {
		INSTANCE_COUNT++;
		var DB_TABNAME = GetInstanceName();

		m_motor = motor;
		m_powertrain = powertrain;
		m_feedforward = feedforward;
		m_pidController = new PIDController(kP, 0, 0);

		m_sim = new FlywheelSim(
			powertrain.getWPILibPlantMotor(),
			powertrain.gearbox.getTotalReduction(),
			moiKgM2
		);

		DashboardManager.addTab(DB_TABNAME);

		nte_motorDutyCycle = DashboardManager.addTabNumberBar(DB_TABNAME, "Duty Cycle", -1.0, 1.0);
		nte_motorOutputVoltage = DashboardManager.addTabNumberBar(DB_TABNAME, "Output Volts", -13.0, 13.0);
		nte_motorInputCurrent = DashboardManager.addTabNumberBar(DB_TABNAME, "Input Amps", -60.0, 60.0);

		var maxRpm = powertrain.getOutputSpeed();
		nte_targetVelocityRpm = DashboardManager.addTabNumberBar(DB_TABNAME, "Target RPM", -maxRpm, maxRpm);
		nte_encVelocityRpm = DashboardManager.addTabNumberBar(DB_TABNAME, "Enc RPM", -maxRpm, maxRpm);

		var maxSurfaceSpeed = powertrain.calculateMetersPerSec(maxRpm);
		nte_encSurfaceSpeedMps = DashboardManager.addTabNumberBar(DB_TABNAME, "Enc Surface Mps", -maxSurfaceSpeed, maxSurfaceSpeed);

		nte_ffEffortVolts = DashboardManager.addTabNumberBar(DB_TABNAME, "FF Effort Volts", -13.0, 13.0);
		nte_pEffortVolts = DashboardManager.addTabNumberBar(DB_TABNAME, "P Effort Volts", -13.0, 13.0);
	}

	/**
	 * Set whether to run in closed loop or open loop
	 * @param closedLoop {@code true} for closed loop, {@code false} for open loop.
	 */
	public void setClosedLoop(boolean closedLoop) {
		m_runClosedLoop = closedLoop;
	}

	/**
	 * Sets the target velocity for the flywheel in RPM.
	 * @param rpm New target RPM.
	 */
	public void setTargetVelocityRpm(double rpm) {
		m_targetVelocityRpm = rpm;
	}

	/**
	 * Call this from a high frequency loop
	 */
	public void periodic() {
		double inputAmps;
		double encoderRpm;

		double outputVoltage = m_motor.getOutputVoltage();
		
		
		if (RobotBase.isReal()) {
			inputAmps = m_motor.getInputCurrent();
			encoderRpm = m_motor.getSensorVelocity();
		} else {
			// update simulation if enabled
			if (DriverStation.isEnabled()) {
				m_sim.setInputVoltage(outputVoltage);
				m_sim.update(0.02);
			}
			
			inputAmps = m_sim.getCurrentDrawAmps();
			encoderRpm =  m_powertrain.calculateMotorRpmFromWheelRpm(m_sim.getAngularVelocityRPM());

			m_motor.getSimCollection().setSensorVelocity(encoderRpm);
		}
		
		updateControlLoops(encoderRpm);
		
		// telemetry
		nte_targetVelocityRpm.setDouble(m_targetVelocityRpm);
		nte_encVelocityRpm.setDouble(encoderRpm);
		nte_motorDutyCycle.setDouble(m_motor.get());
		nte_motorOutputVoltage.setDouble(outputVoltage);
		nte_motorInputCurrent.setDouble(inputAmps);
		nte_encSurfaceSpeedMps.setDouble(m_powertrain.calculateMetersPerSec(encoderRpm));
		nte_ffEffortVolts.setDouble(m_ffEffortVolts);
		nte_pEffortVolts.setDouble(m_pEffortVolts);
	}

	private void updateControlLoops(double currentRpm) {
		double totalEffortVolts = 0;

		// only perform calculations if the desired velocity is not zero.
		if (m_targetVelocityRpm != 0) {
			m_ffEffortVolts = m_feedforward.calculate(currentRpm);

			if (m_runClosedLoop) {
				m_pEffortVolts = m_pidController.calculate(currentRpm, m_targetVelocityRpm);
			} else {
				m_pEffortVolts = 0;
			}
		} else {
			m_ffEffortVolts = 0;
			m_pEffortVolts = 0;
		}

		totalEffortVolts = m_ffEffortVolts + m_pEffortVolts;
		m_motor.setVoltage(totalEffortVolts);
	}
}