package frc.team832.lib.sensors;

import edu.wpi.first.wpilibj.Encoder;

public class REVThroughBoreQuadrature extends Encoder {
    // quadrature mode with index
    public REVThroughBoreQuadrature(int quadAChannel, int quadBChannel, int indexChannel) {
        super(quadAChannel, quadBChannel, indexChannel);
    }

    // quadrature mode
    public REVThroughBoreQuadrature(int quadAChannel, int quadBChannel) {
        super(quadAChannel, quadBChannel);
    }

    @Override
    public double getRate() {
        return (super.getRate() / 2048) * 600;
    }
}
