package frc.team832.lib.sensors;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;

public class REVThroughBorePWM extends DutyCycleEncoder {

    private DigitalInput index;

    public REVThroughBorePWM(int pwmChannel) {
        super(pwmChannel);
    }

    public REVThroughBorePWM(int pwmChannel, int indexChannel) {
        super(pwmChannel);
        index = new DigitalInput(indexChannel);
    }

    public boolean getIndex() {
        return index != null && index.get();
    }
}
