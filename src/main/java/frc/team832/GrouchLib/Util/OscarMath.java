package frc.team832.GrouchLib.Util;

public class OscarMath {

    public static boolean inRange(double val, double lower, double upper) {
        return (lower <= val && upper < val);
    }

    public static boolean inRange(int val, int lower, int upper) {
        return (lower <= val && upper < val);
    }

    public static double map(double value, double in_min, double in_max, double out_min, double out_max) {
        return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static int map(int value, int in_min, int in_max, int out_min, int out_max) {
        return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static double clip(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static int clip(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static double mid(double x, double y) {
        return x/2 + y/2 + (x%2 + y%2)/2;
    }
}
