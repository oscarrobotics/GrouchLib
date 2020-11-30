package frc.team832.lib.sensors;

import edu.wpi.first.wpilibj.Encoder;

public class REVThroughBoreRelative extends Encoder {

    // quadrature mode with index
    public REVThroughBoreRelative(int quadAChannel, int quadBChannel, int indexChannel) {
        super(quadAChannel, quadBChannel, indexChannel);
    }

    // quadrature mode
    public REVThroughBoreRelative(int quadAChannel, int quadBChannel) {
        super(quadAChannel, quadBChannel);
    }

    public REVThroughBoreRelative(int quadAChannel, int quadBChannel, EncodingType type) {
        super(quadAChannel, quadBChannel, false, type);

        setSamplesToAverage(5);
    }

    public REVThroughBoreRelative(int quadAChannel, int quadBChannel, boolean reverseDirection, EncodingType type) {
        super(quadAChannel, quadBChannel, reverseDirection, type);

        setSamplesToAverage(5);
    }
}
