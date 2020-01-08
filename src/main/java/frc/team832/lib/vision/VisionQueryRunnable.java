package frc.team832.lib.vision;

public class VisionQueryRunnable implements Runnable {

	private final VisionSubsystemBase parentSubsystem;
	private VisionTarget target = new VisionTarget();

	public VisionQueryRunnable(VisionSubsystemBase parentSubsystem) {
		this.parentSubsystem = parentSubsystem;
	}

	@Override
	public void run() {
		target.isValid = parentSubsystem.getIsValidEntry().getBoolean(false);
		target.area = parentSubsystem.getAreaEntry().getDouble(0.0);
		target.pitch = parentSubsystem.getPitchEntry().getDouble(0.0);
		target.yaw = parentSubsystem.getYawEntry().getDouble(0.0);
		target.poseMeters = parentSubsystem.getPoseList().get(0);

		parentSubsystem.consumeTarget(target);
	}
}
