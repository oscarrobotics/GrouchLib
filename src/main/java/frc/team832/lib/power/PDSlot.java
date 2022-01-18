package frc.team832.lib.power;

public class PDSlot {
	public enum Breaker {
		TenAmp(10),
		TwentyAmp(20),
		ThirtyAmp(30),
		FortyAmp(40);
	
		public final int ratedCurrent;
	
		Breaker(int ratedCurrent) {
			this.ratedCurrent = ratedCurrent;
		}
	}

	private final GrouchPD pd;
	private final int portNumber;
	private final Breaker breaker;

	protected PDSlot(GrouchPD pd, int portNumber, Breaker breaker) {
		this.pd = pd;
		this.portNumber = portNumber;
		this.breaker = breaker;
	}

	int getPDPortNumber() {
		return portNumber;
	}

	public int getBreakerRatedCurrent() {
		return breaker.ratedCurrent;
	}

	public double getCurrentUsage() {
		return pd.getCurrent(portNumber);
	}

	public double percentOfBreakerUsage() {
		return ((double)breaker.ratedCurrent) / getCurrentUsage();
	}
}
