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

    public Gearbox(GearReduction... reductions) {
        _reductions = new float[reductions.length];

        float tempVal = 1;

        for (int i = 0; i < reductions.length; i++) {
            _reductions[i] = reductions[i].getReduction();
            tempVal = tempVal * _reductions[i];
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
