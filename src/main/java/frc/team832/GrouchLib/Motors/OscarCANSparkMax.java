package frc.team832.GrouchLib.Motors;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.*;
import edu.wpi.first.wpilibj.PIDController;

public class OscarCANSparkMax implements IOscarCANSmartMotor {

	private int _id;

	private double _forwardOutputRange = 0, _reverseOutputRange = 0;

    private CANSparkMax _sparkMax;
    private ControlType _ctrlType;
    private CANSparkMax.ExternalFollower _followType;
    private CANEncoder m_encoder;
    private CANPIDController m_PIDController;

    public OscarCANSparkMax(int canId, CANSparkMaxLowLevel.MotorType mType){
    	_id = canId;
        _sparkMax = new CANSparkMax(canId, mType);
		m_PIDController = _sparkMax.getPIDController();
        m_encoder = _sparkMax.getEncoder();
    }

    @Override
    public void setMode(ControlMode mode) {
    	setMode(mode, 0);
    }

    @Override
    public void setMode(ControlMode mode, double val){
		switch (mode) {
			case PercentOutput:
				_ctrlType = ControlType.kDutyCycle; // percentage of input
				break;
			case Position:
				_ctrlType = ControlType.kPosition;
				break;
			case Velocity:
				_ctrlType = ControlType.kVelocity;
				break;
			case Current:
			case Follower:
			case MotionProfile:
			case MotionMagic:
			case MotionProfileArc:
			case Disabled:
				// Not supported
				break;
		}
		m_PIDController.setReference(val, _ctrlType);
	}

    public void setFollowType(CANSparkMax.ExternalFollower followType) {
    	_followType = followType;
    }

    @Override
    public void follow(int masterMotorID) { _sparkMax.follow(_followType, masterMotorID); }

	@Override
	public void follow(IOscarCANMotor masterMotor) {
		if (masterMotor instanceof OscarCANTalon || masterMotor instanceof OscarCANVictor) {
			_followType = CANSparkMax.ExternalFollower.kFollowerPhoenix;
		} else if (masterMotor instanceof OscarCANSparkMax) {
			_followType = CANSparkMax.ExternalFollower.kFollowerSparkMax;
		} else _followType = CANSparkMax.ExternalFollower.kFollowerDisabled;
		_sparkMax.follow(_followType, masterMotor.getDeviceID());
	}

	@Override
    public int getCurrentPosition() { return (int) _sparkMax.getEncoder().getPosition(); }

    @Override
    public double getInputVoltage() { return _sparkMax.getBusVoltage(); }

    @Override
    public double getOutputVoltage() { return _sparkMax.getAppliedOutput(); }

    @Override
    public double getOutputCurrent() { return _sparkMax.getOutputCurrent(); }

	@Override
	public int getDeviceID() {
		return _sparkMax.getDeviceId();
	}

	@Override
	public int getBaseID() {
		return _id;
	}

	@Override
	public void setNeutralMode(NeutralMode mode) {
		_sparkMax.setIdleMode(mode == NeutralMode.Brake ? CANSparkMax.IdleMode.kBrake : CANSparkMax.IdleMode.kCoast);
	}

	@Override
	public void setSensorPhase(boolean phase) {
		// Not supported
	}

	@Override
	public void setSensor(FeedbackDevice device) {
		//not used
	}

	@Override
	public void setAllowableClosedLoopError(int error) {
		// Not supported
	}

	@Override
	public void setClosedLoopRamp(double secondsFromNeutralToFull) {
		_sparkMax.setRampRate(secondsFromNeutralToFull);
	}

	@Override
	public void setOpenLoopRamp(double secondsFromNeutralToFull) {
		_sparkMax.setRampRate(secondsFromNeutralToFull);
	}

	@Override
	public int getSensorVelocity() {
		return (int) _sparkMax.getEncoder().getVelocity();
	}

	@Override
	public int getSensorPosition() {
		return (int) _sparkMax.getEncoder().getPosition();
	}

	@Override
	public void setSensorPosition(int absolutePosition) {
		// Not supported, coming in future API
	}

	@Override
	public double getTargetPosition() {
    	return 0.0; // Not supported
	}

	@Override
	public boolean getForwardLimitSwitch() {
		return _sparkMax.getForwardLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen).isLimitSwitchEnabled();
	}

	@Override
	public boolean getReverseLimitSwitch() {
		return _sparkMax.getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen).isLimitSwitchEnabled();
	}

	@Override
	public int getClosedLoopError() {
		return 0; // Not supported
	}

	@Override
	public int getPulseWidthPosition() {
		return (int) _sparkMax.getEncoder().getPosition(); // Is this correct?
	}

	@Override
	public void set_kF(int slot, double kF) {
		_sparkMax.getPIDController().setFF(kF, slot);
	}

	@Override
	public void setPeakOutputForward(double percentOut) {
    	_forwardOutputRange = percentOut;
		_sparkMax.getPIDController().setOutputRange(_forwardOutputRange, _reverseOutputRange);
	}

	@Override
	public void setPeakOutputReverse(double percentOut) {
		_reverseOutputRange = percentOut;
		_sparkMax.getPIDController().setOutputRange(_forwardOutputRange, _reverseOutputRange);
	}

	@Override
	public void setNominalOutputForward(double percentOut) {
		// Not supported
	}

	@Override
	public void setNominalOutputReverse(double percentOut) {
		// Not supported
	}

	@Override
	public void setkP(double kP) {
		m_PIDController.setP(kP);
	}

	@Override
	public void setkI(double kI) {
		m_PIDController.setP(kI);
	}

	@Override
	public void setkD(double kD) {
		m_PIDController.setP(kD);
	}

	@Override
	public void setkF(double kF) {
		m_PIDController.setP(kF);
	}

	@Override
	public void setkP(double kP, int slotID) {
		_sparkMax.getPIDController().setP(kP, slotID);
	}

	@Override
	public void setkI(double kI, int slotID) {
		_sparkMax.getPIDController().setP(kI, slotID);
	}

	@Override
	public void setkD(double kD, int slotID) {
		_sparkMax.getPIDController().setP(kD, slotID);
	}

	@Override
	public void setkF(double kF, int slotID) {
		_sparkMax.getPIDController().setP(kF, slotID);
	}

	@Override
	public void setUpperLimit(int limit) {
		//not yet implemented
	}

	@Override
	public void setLowerLimit(int limit) {
		//not yet implemented
	}

	@Override
    public void set(double value) { _sparkMax.set(value); }

    @Override
    public double get() { return _sparkMax.get(); }

    @Override
    public void setInverted(boolean isInverted) { _sparkMax.setInverted(isInverted); }

    @Override
    public boolean getInverted() { return _sparkMax.getInverted(); }

    @Override
    public void disable() { _sparkMax.disable(); }

    public void resetSensor(){
        //not yet implemented
    }

    public CANSparkMax getInstance() {
    	return _sparkMax;
	}

    @Override
    public void stopMotor() { _sparkMax.stopMotor(); }

    public void setReference(double x, ControlType type){
    	m_PIDController.setReference(x, type);
	}

	public void setOutputRange(double min, double max) {
		m_PIDController.setOutputRange(min, max);
	}
}

