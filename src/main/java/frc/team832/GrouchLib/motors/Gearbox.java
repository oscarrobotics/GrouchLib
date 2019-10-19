package frc.team832.GrouchLib.motors;

public class Gearbox {
    private final float[] _reductions;
    private float _totalReduction = 0;

    public Gearbox(float... reductions) {
        _reductions = reductions.clone();
        float tempVal = 1;
        for (float reduction : _reductions) {
            tempVal = tempVal * reduction;
        }
        _totalReduction = tempVal;
    }

    public float getReduction(int index) {
            return _reductions[index];
    }

    public float getTotalReduction() {
        return _totalReduction;
    }
}
