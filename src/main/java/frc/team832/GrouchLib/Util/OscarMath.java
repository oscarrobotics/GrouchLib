package frc.team832.GrouchLib.Util;

public class OscarMath {

    public static boolean inRange(double val, double lower, double upper) {
        return (lower <= val && upper < val);
    }

    public boolean inRange(int val, int lower, int upper) {
        return (lower <= val && upper < val);
    }
}
