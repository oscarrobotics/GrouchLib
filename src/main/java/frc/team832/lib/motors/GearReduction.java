package frc.team832.lib.motors;

public class GearReduction {
    private final int driving;
    private final int driven;

    public GearReduction(int driving, int driven) {
        this.driving = driving;
        this.driven = driven;
    }

    public float getReduction() {
        return (float) driving / (float) driven;
    }
}
