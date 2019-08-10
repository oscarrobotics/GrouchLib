package frc.team832.GrouchLib.motors;

public class Gearbox {
    private final long[] _reductions;
    private long _totalReduction = 0;

    public Gearbox(long... reductions) {
        _reductions = reductions.clone();
        long tempVal = 1;
        for(int i = 0; i < _reductions.length; i++) {
            tempVal = Math.multiplyExact(tempVal, _reductions[i]);
        }
    }

    public long getReduction(int index) {
            return _reductions[index];
    }

    public void setTotalReduction(long totalReduction) {
        _totalReduction = totalReduction;
    }

    public long getTotalReduction() {
        return _totalReduction;
    }
}
