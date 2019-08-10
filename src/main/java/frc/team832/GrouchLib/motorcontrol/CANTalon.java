package frc.team832.GrouchLib.motorcontrol;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team832.GrouchLib.CANDevice;

/**
 * Implementation of IOscarSmartMotor that is specific to a CTRE Talon SRX
 */
public class CANTalon implements GeniusMC {

    private TalonSRX _talon;
    private ControlMode _ctrlMode;
    private int _allowableError = 0;

    private BufferedTrajectoryPointStream _bufferedStream = new BufferedTrajectoryPointStream();
    int profilePointCount = 0;
	
    /***
     * Create an OscarCANTalon at the specified CAN ID.
     * @param canId CAN ID of controller to attach.
     */
    public CANTalon(int canId) {
        _talon = new TalonSRX(canId);
        _ctrlMode = ControlMode.Disabled;

        boolean onBus = _talon.getBusVoltage() > 0.0; // TODO: better way to do this?
        CANDevice.addDevice(new CANDevice(canId, onBus, "Talon SRX"));

    }

    @Override
    public void set(double value) {
        _talon.set(_ctrlMode, value);
    }

    public void set(ControlMode ctrlMode, double value) {
        _ctrlMode = ctrlMode;
        _talon.set(_ctrlMode, value);
    }

    @Override
    public double get() {
        return _talon.getMotorOutputPercent();
    }


    @Override
    public boolean getInverted() {
        return _talon.getInverted();
    }

    @Override
    public void setInverted(boolean isInverted) {
        _talon.setInverted(isInverted);
    }

    @Override
    public void disable() {
        _ctrlMode = ControlMode.Disabled;
        set(0);
    }

    @Override
    public void stopMotor() {
        _ctrlMode = ControlMode.PercentOutput;
        set(0);
    }

    @Override
    public void follow(int masterMotorID) {
        _ctrlMode = ControlMode.Follower;
        set(masterMotorID);
    }

    public void follow(TalonSRX talon){
        _talon.follow(talon);
    }

    @Override
    public void follow(CANMC masterMotor) {
        follow(masterMotor.getDeviceID());
    }

    @Override
    public double getInputVoltage() {
        return _talon.getBusVoltage();
    }

    @Override
    public double getOutputVoltage() {
        return _talon.getMotorOutputVoltage();
    }

    @Override
    public double getOutputCurrent() {
        return _talon.getOutputCurrent();
    }

    @Override
    public void resetSettings() {
        _talon.configFactoryDefault();
    }

    @Override
    public int getDeviceID() {
        return _talon.getDeviceID();
    }

    @Override
    public int getBaseID() {
        return _talon.getBaseID();
    }

    @Override
    public void setNeutralMode(frc.team832.GrouchLib.motorcontrol.NeutralMode mode) {
        _talon.setNeutralMode(mode == NeutralMode.kBrake ? com.ctre.phoenix.motorcontrol.NeutralMode.Brake : com.ctre.phoenix.motorcontrol.NeutralMode.Coast);
    }

    public void setSensorPhase(boolean phase) {
        _talon.setSensorPhase(phase);
    }

    public void setSensorType(FeedbackDevice device) {
        _talon.configSelectedFeedbackSensor(device);
    }

//    public void configureContinuousCurrent(int amps){
//        _talon.configContinuousCurrentLimit(amps);
//    }

    @Override
    public void setAllowableClosedLoopError(int error) {
        _allowableError = error;
        _talon.configAllowableClosedloopError(0, error);
    }

    @Override
    public int getAllowableClosedLoopError() {
        return _allowableError;
    }

    @Override
    public void setClosedLoopRamp(double secondsFromNeutralToFull) {
        _talon.configClosedloopRamp(secondsFromNeutralToFull);
    }

    @Override
    public void setOpenLoopRamp(double secondsFromNeutralToFull) {
        _talon.configOpenloopRamp(secondsFromNeutralToFull);
    }

    @Override
    public double getSensorVelocity() {
       return _talon.getSelectedSensorVelocity();
    }

    @Override
    public double getSensorPosition() {
        return _talon.getSelectedSensorPosition();
    }

    @Override
    public void setSensorPosition(int absolutePosition) {
        _talon.setSelectedSensorPosition(absolutePosition);
    }

    @Override
    public double getTargetPosition() {
        if(_ctrlMode!=ControlMode.Disabled&&_ctrlMode!=ControlMode.PercentOutput)
        return _talon.getClosedLoopTarget();
        return 0;
    }

    @Override
    public boolean getForwardLimitSwitch() {
        return _talon.getSensorCollection().isFwdLimitSwitchClosed();
    }

    @Override
    public boolean getReverseLimitSwitch() {
        return _talon.getSensorCollection().isRevLimitSwitchClosed();
    }

    @Override
    public int getClosedLoopError() {
        return _talon.getClosedLoopError();
    }


    @Override
    public void setPeakOutputForward(double percentOut) {
        _talon.configPeakOutputForward(percentOut);
    }

    @Override
    public void setPeakOutputReverse(double percentOut) { _talon.configPeakOutputReverse(percentOut);
    }

    @Override
    public void setNominalOutputForward(double percentOut) {

    }

    @Override
    public void setNominalOutputReverse(double percentOut) {

    }

    @Override
    public void setkP(double kP) {
        _talon.config_kP(0, kP);
    }

    @Override
    public void setkI(double kI) {
        _talon.config_kI(0, kI);
    }

    @Override
    public void setkD(double kD) {
        _talon.config_kD(0, kD);
    }

    @Override
    public void setkF(double kF) {
        _talon.config_kF(0, kF);
    }

    @Override
    public void setkP(double kP, int slotID) {
        _talon.config_kP(slotID, kP);
    }

    @Override
    public void setkI(double kI, int slotID) {
        _talon.config_kI(slotID, kI);
    }

    @Override
    public void setkD(double kD, int slotID) {
        _talon.config_kD(slotID, kD);
    }

    @Override
    public void setkF(double kF, int slotID) {
        _talon.config_kF(slotID, kF);
    }

    @Override
    public void setForwardSoftLimit(int limit) {
        _talon.configForwardSoftLimitThreshold(limit);
        _talon.configForwardSoftLimitEnable(true);
    }

    @Override
    public void setReverseSoftLimit(int limit) {
        _talon.configReverseSoftLimitThreshold(limit);
        _talon.configReverseSoftLimitEnable(true);
    }

    @Override
    public void resetSensor(){
        _talon.setSelectedSensorPosition(0);
    }

    @Override
    public void setVelocity(double rpmVal) {
        _ctrlMode = ControlMode.Velocity;
        _talon.set(_ctrlMode, rpmVal);
    }

    @Override
    public void setPosition(double posVal) {
        _ctrlMode = ControlMode.Position;
        _talon.set(_ctrlMode, posVal);
    }

    @Override
    public void setArbFFPos(double arbFF, double pos) {
        _ctrlMode = ControlMode.Position;
        _talon.set(_ctrlMode, pos, DemandType.ArbitraryFeedForward, arbFF);
    }

    @Override
    public void setPeakCurrentLimit(int peak) {
        _talon.configPeakCurrentLimit(peak);
    }


    public void configMotionMagic(int sensorUnitsPer100ms, int sensorUnitsPer100MsPerSec){
        _talon.configMotionCruiseVelocity(sensorUnitsPer100ms);
        _talon.configMotionAcceleration(sensorUnitsPer100MsPerSec);
        _talon.configMotionSCurveStrength(3);

    }

    public void setMotionMagic(double pos){
        _ctrlMode = ControlMode.MotionMagic;
        _talon.selectProfileSlot(0,0);
        _talon.set(_ctrlMode, pos);
    }

    @Override
    public boolean atTarget() {
        return Math.abs(getTargetPosition()- getSensorPosition()) < 20;
    }

    @Override
    public void fillProfileBuffer(double[][] profile, int totalCnt) {
        _bufferedStream.Clear();
        profilePointCount = totalCnt;
        TrajectoryPoint point = new TrajectoryPoint();

        for (int i = 0; i < totalCnt; ++i) {
            double positionRot = profile[i][1];
            double velocityRPM = profile[i][2];

            /* for each point, fill our structure and pass it to API */
            point.timeDur = (int) profile[i][0]*1000;
            point.position = positionRot; //* Constants.kSensorUnitsPerRotation; // Convert Revolutions to Units
            point.velocity = velocityRPM; //* Constants.kSensorUnitsPerRotation / 600.0; // Convert RPM to Units/100ms
            point.auxiliaryPos = 0;
            point.auxiliaryVel = 0;
            point.profileSlotSelect0 = 0; /* which set of gains would you like to use [0,3]? */
            point.profileSlotSelect1 = 0; /* auxiliary PID [0,1], leave zero */
            point.zeroPos = false; /* set this to true on the first point if you want to zero the sensor*/
            point.isLastPoint = ((i + 1) == totalCnt); /* set this to true on the last point */
            point.arbFeedFwd = 0; /* you can add a constant offset to add to PID[0] output here */

            _bufferedStream.Write(point);
        }

        System.out.println("LP: " + point.isLastPoint);
        System.out.println(String.format("Pushed %d points to Talon %d.", totalCnt, getBaseID()));
    }

    @Override
    public void setMotionProfile(int value) {
        _talon.set(ControlMode.MotionProfile, value);
    }

    @Override
    public void bufferAndSendMP() {
        _talon.startMotionProfile(_bufferedStream, 25, ControlMode.MotionProfile);
    }

    @Override
    public ErrorCode getMotionProfileStatus(MotionProfileStatus status) {
        return _talon.getMotionProfileStatus(status);
    }

    @Override
    public boolean isMPFinished() {
        return _talon.isMotionProfileFinished();
    }

    @Override
    public void setIZone(int iZone) {
        _talon.config_IntegralZone(0, iZone);
    }

    @Override
    public void setMotionMagcArbFF (double position, double arbFF) {
        _ctrlMode = ControlMode.MotionMagic;
        _talon.set(_ctrlMode, position, DemandType.ArbitraryFeedForward, arbFF);
    }
}

