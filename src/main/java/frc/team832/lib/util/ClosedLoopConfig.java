package frc.team832.lib.util;

public class ClosedLoopConfig {
    private double kP, kI, kD, kF;
    private int slotIDx = 0;

    public ClosedLoopConfig(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }

    public double getkP() { return kP; }
    public double getkI() { return kI; }
    public double getkD() { return kD; }
    public double getkF() { return kF; }
    public int getSlotIDx() { return slotIDx; }
}
