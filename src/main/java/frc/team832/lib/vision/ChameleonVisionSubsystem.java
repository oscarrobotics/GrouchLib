package frc.team832.lib.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.geometry.Pose2d;

import java.util.List;

public abstract class ChameleonVisionSubsystem extends VisionSubsystemBase {

	private final NetworkTable rootTable;
	private final NetworkTableEntry areaEntry;
	private final NetworkTableEntry pitchEntry;
	private final NetworkTableEntry yawEntry;
	private final NetworkTableEntry isValidEntry;

	public ChameleonVisionSubsystem(String cameraName, double queryPeriod) {
		super(queryPeriod);

		this.rootTable = NetworkTableInstance.getDefault().getTable(cameraName);
		this.areaEntry = rootTable.getEntry("targetArea");
		this.pitchEntry = rootTable.getEntry("targetPitch");
		this.yawEntry = rootTable.getEntry("targetYaw");
		this.isValidEntry = rootTable.getEntry("isValid");
	}

	@Override
	public NetworkTable getRootTable () {
		return rootTable;
	}

	@Override
	public NetworkTableEntry getAreaEntry () { return areaEntry; }

	@Override
	public NetworkTableEntry getPitchEntry () {
		return pitchEntry;
	}

	@Override
	public NetworkTableEntry getYawEntry () {
		return yawEntry;
	}

	@Override
	public NetworkTableEntry getIsValidEntry () {
		return isValidEntry;
	}

	@Override
	public List<Pose2d> getPoseList () {
		return List.of();
	}

	@Override
	public String getDashboardTabName () {
		return "ChameleonVision";
	}

	@Override
	public void updateDashboardData () {

	}
}
