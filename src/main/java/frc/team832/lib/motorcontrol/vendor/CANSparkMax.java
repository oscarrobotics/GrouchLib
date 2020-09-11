package frc.team832.lib.motorcontrol.vendor;

import com.revrobotics.*;
import frc.team832.lib.motorcontrol.NeutralMode;
import frc.team832.lib.motorcontrol.base.CANMC;
import frc.team832.lib.motorcontrol.base.SmartCANMC;

/**
 * @deprecated Use {@link frc.team832.lib.motorcontrol2.vendor.CANSparkMax} instead.
 *
 */
@Deprecated
public class CANSparkMax implements SmartCANMC<com.revrobotics.CANSparkMax> {

	private int _id;
	private double _openLoopSetpoint, _closedLoopSetpoint;
	private double _forwardOutputRange = 0, _reverseOutputRange = 0;
	private double _allowableError = 0;
	private double _closedLoopRampRate = 0;

	private final com.revrobotics.CANSparkMax _sparkMax;
	private ControlType _ctrlType;
	private com.revrobotics.CANSparkMax.ExternalFollower _followType;
	private final CANEncoder _encoder;
	private final CANPIDController _pidController;

	public CANSparkMax(int canId, CANSparkMaxLowLevel.MotorType mType) {
		_id = canId;
		_sparkMax = new com.revrobotics.CANSparkMax(canId, mType);

//		boolean onBus = _sparkMax.getFirmwareString() != null;
//		CANDeviceManager.addDevice(_id, onBus, "Spark MAX");

		_pidController = _sparkMax.getPIDController();
		_encoder = _sparkMax.getEncoder();
	}

	@Override
	public void setVelocity(double rpmVal) {
		_closedLoopSetpoint = rpmVal;
		_ctrlType = ControlType.kVelocity;
		_pidController.setReference(rpmVal, _ctrlType);
	}

	@Override
	public void setPosition(double posVal) {
		_closedLoopSetpoint = posVal;
		_ctrlType = ControlType.kPosition;
		_pidController.setReference(posVal, _ctrlType);
	}

	@Override
	public void setArbFFPos(double arbFF, double pos) {
		//not yet implemented
	}

	@Override
	public void setPeakCurrentLimit(int peak) {
		_sparkMax.setSmartCurrentLimit(peak);
	}

	@Override
	public boolean atTarget() {
		switch (_ctrlType) {
			case kDutyCycle:
			case kVoltage:
				return true;
			case kPosition:
			case kCurrent:
			case kVelocity:
				return Math.abs(getClosedLoopError()) <= _allowableError;
			case kSmartMotion:
			case kSmartVelocity:
				return false; // TODO: Figure out how to do this
		}
		return false;
	}

	public void setFollowType(com.revrobotics.CANSparkMax.ExternalFollower followType) {
		_followType = followType;
	}

	@Override
	public com.revrobotics.CANSparkMax getBaseController() {
		return _sparkMax;
	}

	@Override
	public void follow(int masterMotorID) {
		_sparkMax.follow(_followType, masterMotorID);
	}

	@Override
	public void follow(CANMC masterMotor) {
		if (masterMotor instanceof CANTalon || masterMotor instanceof CANVictor) {
			_followType = com.revrobotics.CANSparkMax.ExternalFollower.kFollowerPhoenix;
		} else if (masterMotor instanceof CANSparkMax) {
			_followType = com.revrobotics.CANSparkMax.ExternalFollower.kFollowerSparkMax;
		} else _followType = com.revrobotics.CANSparkMax.ExternalFollower.kFollowerDisabled;
		_sparkMax.follow(_followType, masterMotor.getDeviceID());
	}

	@Override
	public double getInputVoltage() {
		return _sparkMax.getBusVoltage();
	}

	@Override
	public double getOutputVoltage() {
		return _sparkMax.getAppliedOutput();
	}

	@Override
	public double getOutputCurrent() {
		return _sparkMax.getOutputCurrent();
	}

	@Override
	public void resetSettings() {
		_sparkMax.restoreFactoryDefaults();
	}

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
		_sparkMax.setIdleMode(mode == NeutralMode.kBrake ? com.revrobotics.CANSparkMax.IdleMode.kBrake : com.revrobotics.CANSparkMax.IdleMode.kCoast);
	}

	@Override
	public void setAllowableClosedLoopError(double error) {
		_allowableError = error;
	}

	@Override
	public double getAllowableClosedLoopError() {
		return _allowableError;
	}

	@Override
	public void setClosedLoopRamp(double secondsFromNeutralToFull) {
		if (secondsFromNeutralToFull != _closedLoopRampRate) {
			_closedLoopRampRate = secondsFromNeutralToFull;
			_sparkMax.setClosedLoopRampRate(secondsFromNeutralToFull);
		}
	}

	@Override
	public void setOpenLoopRamp(double secondsFromNeutralToFull) {
		_sparkMax.setOpenLoopRampRate(secondsFromNeutralToFull);
	}

	@Override
	public double getSensorPosition() {
		return _encoder.getPosition();
	}

	@Override
	public double getSensorVelocity() {
		return _encoder.getVelocity();
	}

	@Override
	public void setSensorPosition(int absolutePosition) {
		_encoder.setPosition(absolutePosition);
	}

	@Override
	public double getClosedLoopTarget() {
		switch (_ctrlType) {
			case kCurrent:
			case kPosition:
			case kVelocity:
				return _closedLoopSetpoint;
			case kDutyCycle:
			case kVoltage:
			case kSmartMotion:
			case kSmartVelocity:
				return 0.0;
			default:
				return 0.0;
		}
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
	public double getClosedLoopError() {
		switch (_ctrlType) {
			case kPosition:
				return _closedLoopSetpoint - getSensorPosition();
			case kVelocity:
				return _closedLoopSetpoint - getSensorVelocity();
			case kCurrent:
				return _closedLoopSetpoint - getOutputCurrent();
		}
		return 0;
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
		_pidController.setP(kP);
	}

	@Override
	public void setkI(double kI) {
		_pidController.setI(kI);
	}

	@Override
	public void setkD(double kD) {
		_pidController.setD(kD);
	}

	@Override
	public void setkF(double kF) {
		_pidController.setFF(kF);
	}

	@Override
	public void setkP(double kP, int slotID) {
		_pidController.setP(kP, slotID);
	}

	@Override
	public void setkI(double kI, int slotID) {
		_pidController.setI(kI, slotID);
	}

	@Override
	public void setkD(double kD, int slotID) {
		_pidController.setD(kD, slotID);
	}

	@Override
	public void setkF(double kF, int slotID) {
		_pidController.setFF(kF, slotID);
	}

	@Override
	public void setForwardSoftLimit(int limit) {
		//not yet implemented
	}

	@Override
	public void setReverseSoftLimit(int limit) {
		//not yet implemented
	}

	@Override
	public void rezeroSensor() {
		setSensorPosition(0);
	}

	@Override
	public void set(double value) {
		_openLoopSetpoint = value;
		_sparkMax.set(value);
	}

	@Override
	public double get() {
		return _sparkMax.get();
	}

	@Override
	public void setInverted(boolean isInverted) {
		_sparkMax.setInverted(isInverted);
	}

	@Override
	public boolean getInverted() {
		return _sparkMax.getInverted();
	}

	@Override
	public void disable() {
		_sparkMax.disable();
	}

	public com.revrobotics.CANSparkMax getInstance() {
		return _sparkMax;
	}

	@Override
	public void stopMotor() {
		_sparkMax.stopMotor();
	}

	public void setOutputRange(double min, double max) {
		_pidController.setOutputRange(min, max);
	}

	@Override
	public void setMotionMagic(double pos){};
}


