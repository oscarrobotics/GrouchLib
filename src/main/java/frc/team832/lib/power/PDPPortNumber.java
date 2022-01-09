package frc.team832.lib.power;

public enum PDPPortNumber {
	Port0(PDPBreaker.FortyAmp),
	Port1(PDPBreaker.FortyAmp),
	Port2(PDPBreaker.FortyAmp),
	Port3(PDPBreaker.FortyAmp),
	Port4(PDPBreaker.TwentyAmp, PDPBreaker.ThirtyAmp),
	Port5(PDPBreaker.TwentyAmp, PDPBreaker.ThirtyAmp),
	Port6(PDPBreaker.TwentyAmp, PDPBreaker.ThirtyAmp),
	Port7(PDPBreaker.TwentyAmp, PDPBreaker.ThirtyAmp),
	Port8(PDPBreaker.TwentyAmp, PDPBreaker.ThirtyAmp),
	Port9(PDPBreaker.TwentyAmp, PDPBreaker.ThirtyAmp),
	Port10(PDPBreaker.TwentyAmp, PDPBreaker.ThirtyAmp),
	Port11(PDPBreaker.TwentyAmp, PDPBreaker.ThirtyAmp),
	Port12(PDPBreaker.FortyAmp),
	Port13(PDPBreaker.FortyAmp),
	Port14(PDPBreaker.FortyAmp),
	Port15(PDPBreaker.FortyAmp);

	PDPPortNumber(PDPBreaker minBreaker, PDPBreaker maxBreaker) {
		this.maxBreaker = maxBreaker;
		this.minBreaker = minBreaker;
	}

	PDPPortNumber(PDPBreaker maxBreaker) {
		this.maxBreaker = maxBreaker;
		this.minBreaker = maxBreaker;
	}

	public final PDPBreaker minBreaker;
	public final PDPBreaker maxBreaker;
}
