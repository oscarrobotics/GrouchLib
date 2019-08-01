package frc.team832.GrouchLib.Motors;

public enum NeutralMode {
    kCoast(0),
    kBrake(1);

    public int value;
    NeutralMode(int value)
    {
        this.value = value;
    }
}
