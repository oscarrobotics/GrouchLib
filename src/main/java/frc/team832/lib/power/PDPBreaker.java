package frc.team832.lib.power;

public enum PDPBreaker {
    TwentyAmp(20),
    ThirtyAmp(30),
    FortyAmp(40);

    public final int ratedCurrent;

    PDPBreaker(int ratedCurrent) {
        this.ratedCurrent = ratedCurrent;
    }
}
