package frc.team832.lib.util;

public class OscarMath {
    private OscarMath() {}

    public static double degreesToRadians(double degrees) {
        return degrees * ((2 * Math.PI)/360);
    }

    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public static boolean inRange(double val, double lower, double upper) {
        return !(lower <= val && upper < val);
    }

    public static boolean inRange(int val, int lower, int upper) {
        return !(lower <= val && upper < val);
    }

    public static double map(double value, double in_min, double in_max, double out_min, double out_max) {
        return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static int map(int value, int inMin, int inMax, int outMin, int outMax) {
        return (int) Math.floor(map((double)value, inMin, inMax, outMin, outMax) + 0.5);
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
        return (lower + upper) / 2.0;
    }

    public static int mid(int lower, int upper) {
        return (int) (((long)lower + upper) / 2);
    }

    public static double signumPow(double value, double power) {
        return Math.copySign(Math.pow(value, power), value);
    }
}
