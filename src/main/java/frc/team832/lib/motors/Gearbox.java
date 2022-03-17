package frc.team832.lib.motors;

public class Gearbox {
	private final double[] _reductions;
	private final double _totalReduction;

	public Gearbox(double... reductions) {
		_reductions = reductions.clone();
		double tempVal = 1;
		for (double reduction : _reductions) {
			tempVal = tempVal * reduction;
		}
		_totalReduction = 1 / tempVal;
	}

	protected double getReduction(int index) {
		return _reductions[index];
	}

	public double getTotalReduction() {
		return _totalReduction;
	}
}
