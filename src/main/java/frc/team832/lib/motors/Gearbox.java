package frc.team832.lib.motors;

public class Gearbox {
    private final float[] _reductions;
    private final float _totalReduction;

    public Gearbox(float... reductions) {
        _reductions = reductions.clone();
        float tempVal = 1;
        for (float reduction : _reductions) {
            tempVal = tempVal * reduction;
        }
        _totalReduction = tempVal;
    }

    protected float getReduction(int index) {
        return _reductions[index];
    }

    public float getTotalReduction() {
        return _totalReduction;
    }
}
