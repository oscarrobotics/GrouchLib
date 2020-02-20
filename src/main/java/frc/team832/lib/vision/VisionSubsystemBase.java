package frc.team832.lib.vision;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team832.lib.driverstation.dashboard.DashboardUpdatable;

import java.util.List;

public abstract class VisionSubsystemBase extends SubsystemBase {

	private final VisionQueryRunnable visionQueryRunnable = new VisionQueryRunnable(this);

	private final Notifier ntQueryNotifier;

	public VisionSubsystemBase(double queryPeriod) {
		ntQueryNotifier = new Notifier(visionQueryRunnable);
		ntQueryNotifier.startPeriodic(queryPeriod);
	}

	public abstract NetworkTable getRootTable();
	public abstract NetworkTableEntry getAreaEntry();
	public abstract NetworkTableEntry getPitchEntry();
	public abstract NetworkTableEntry getYawEntry();
	public abstract NetworkTableEntry getIsValidEntry();

	public abstract List<Pose2d> getPoseList();

	public abstract void consumeTarget(VisionTarget target);
}
