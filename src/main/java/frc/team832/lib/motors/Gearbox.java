package frc.team832.lib.motors;

public class Gearbox {
	private final double[] _reductions;
	public final double totalReduction;
	public final int numStages;

	/**
	 *
	 * @param reductions Gear reduction expressed as Driving / Driven, i.e. {@code 11.0 / 60.0}
	 */
	public Gearbox(double... reductions) {
		_reductions = reductions.clone();
		double tempVal = 1;
		int stages = 0;
		
		for (double reduction : _reductions) {
			stages++;
			tempVal = tempVal * reduction;
		}
		totalReduction = 1 / tempVal;
		numStages = stages;
	}

	protected double getReduction(int index) {
		return _reductions[index];
	}
}
