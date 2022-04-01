package frc.team832.lib.motors;

/**
 * Represents a physical gearbox, and its stages.
 * Does not necessarily involve gears - any reduction or overdrive method such as belt or chain is valid.
 */
public class Gearbox {
	private final double[] _reductions;

	/**
	 * Total reduction of the gearbox.
	 */
	public final double totalReduction;

	/**
	 * Number of stages in the gearbox.
	 */
	public final int numStages;

	/**
	 * 
	 * @param totalReduction Total gearbox reduction, i.e. {@code 10.71} for a 10.71:1 reduction.
	 * 	To represent an overdrive/"upduction", swap the operands. i.e. {@code 0.5} or {@code 1.0 / 2.0} for a 1:2 overdrive.
	 * @return {@link Gearbox} with the given total reduction
	 */
	public static Gearbox fromTotalReduction(double totalReduction) {
		return new Gearbox(1.0 / totalReduction);
	}

	/**
	 * @param stages Reduction stages expressed as Driving / Driven, i.e. {@code 11.0 / 60.0}
	 * @return {@link Gearbox} built from specified stages
	 */
	public static Gearbox fromStages(double... stages) {
		return new Gearbox(stages);
	}

	private Gearbox(double... reductions) {
		_reductions = reductions.clone();
		double tempVal = 1;
		int stages = 0;
		
		for (double reduction : _reductions) {
			stages++;
			tempVal = tempVal *  (1 / reduction);
		}
		totalReduction = tempVal;
		numStages = stages;
	}

	/**
	 * Dangerous. Will happily throw exceptions.
	 * @throws IndexOutOfBoundsException
	 */
	protected double getReduction(int index) {
		if (index > numStages - 1) throw new IndexOutOfBoundsException();
		return 1 / _reductions[index];
	}

	/**
	 * Get the output for the given input.
	 */
	public double getInputToOutput(double input) {
		return input / totalReduction;
	}

	/**
	 * Get the input for the given output.
	 */
	public double getOutputToInput(double output) {
		return output * totalReduction;
	}
}
