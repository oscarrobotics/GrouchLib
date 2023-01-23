// Awaiting LibCu 2022 update

// package frc.team832.lib.sensors.digital;

// import com.cuforge.libcu.Lasershark;

// import edu.wpi.first.math.util.Units;
// import edu.wpi.first.util.sendable.Sendable;
// import frc.team832.lib.sensors.base.DistanceSensor;

// public class LasersharkDistance extends DistanceSensor {

// 	private static final double kMinRangeMeters = Units.inchesToMeters(2);
// 	private static final double kMaxRangeMeters = Units.feetToMeters(12);

// 	private final Lasershark shark;

// 	public LasersharkDistance(int digitalChannel) {
// 		super(kMinRangeMeters, kMaxRangeMeters);
// 		shark = new Lasershark(digitalChannel);
// 	}

// 	@Override
// 	public double getDistanceMeters() {
// 		return shark.getDistanceMeters();
// 	}
// }
