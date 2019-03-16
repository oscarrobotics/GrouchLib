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

    public static double clipMap(double value, double in_min, double in_max, double out_min, double out_max) {
        return map(clip(value, in_min, in_max), in_min, in_max, out_min, out_max);
    }

    public static double mid(double lower, double upper) {
        return lower/2 + upper/2 + (lower%2 + upper%2)/2;
    }

    public static double signumPow(double value, double power) {
        return Math.signum(value) * Math.abs(Math.pow(value, power));
    }
}
