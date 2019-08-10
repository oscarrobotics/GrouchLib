package frc.team832.GrouchLib.util;

public class Tuple<X, Y> {
	public final X primary;
	public final Y secondary;

	public Tuple(X x, Y y) {
		this.primary = x;
		this.secondary = y;
	}
}
