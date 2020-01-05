package frc.team832.lib.motors;

public class Motor {

    public final double freeSpeed;
    public final double freeCurrent;
    public final double stallTorque;
    public final double stallCurrent;
	
    public final double r;
	public final double kv;
    public final double kt;

    public Motor(double _freeSpeed, double _freeCurrent, double _stallTorque, double _stallCurrent, double _r, double _kv, double _kt) {
        freeSpeed = _freeSpeed;
        freeCurrent = _freeCurrent;
        stallTorque = _stallTorque;
        stallCurrent = _stallCurrent;
		
        r = _r;
		kv = _kv;
        kt = _kt;
    }

    public Motor(double _freeSpeed, double _freeCurrent, double _stallTorque, double _stallCurrent) {
        freeSpeed = _freeSpeed;
        freeCurrent = _freeCurrent;
        stallTorque = _stallTorque;
        stallCurrent = _stallCurrent;
		
		r = 12 / _stallCurrent;
        kv = _freeSpeed / (12 - _freeCurrent * r);
        kt = _stallTorque / _stallCurrent;
    }


    public double predictiveCurrentLimit(double currentV, double maxI, double currentRPM) {
        double currentI = currentV / r - currentRPM / (kv * r);
        double outputV = currentV;

        if (currentI > maxI) {
            outputV = maxI * r + currentRPM / kv;
        }
        return outputV;
    }

    public double reactiveCurrentLimit(double currentV, double maxI, double currentI) {
        double omega = kv * currentV - currentI * r * kv;
        double outputV = currentV;

        if (currentI > maxI) {
            outputV = maxI * r + omega / kv;
        }
        return outputV;
    }

    public static final Motor kCIM = new Motor(5330, 2.7, 2.41, 131, 0.0916, 453.514, 0.0184);
    public static final Motor kMiniCIM = new Motor(5840, 3, 1.41, 89);
    public static final Motor kNEO = new Motor(5880, 1.3, 3.36, 166);
    public static final Motor kNEO550 = new Motor(11000, 1.4, 0.97, 100); 
    public static final Motor kFalcon500 = new Motor(6380, 1.5, 4.69, 257); // TODO: Experimential values dont exist?
    public static final Motor kBAG = new Motor(13180, 1.8, 0.43, 53);
    public static final Motor k775Pro = new Motor(18730, 0.7, 0.71, 134);
    public static final Motor kAndyMark9015 = new Motor(14720, 3.7, 0.36, 71);
    public static final Motor kAndyMarkNeveRest = new Motor(5480, 0.4, 0.17, 10);
    public static final Motor kRS775_125 = new Motor(5800, 1.6, 0.28, 18);
    public static final Motor kRS775_18V = new Motor(13050, 2.7, 0.72, 97);
    public static final Motor kRS550 = new Motor(19000, 0.4, 0.38, 84);
}