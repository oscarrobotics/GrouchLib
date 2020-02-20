package frc.team832.lib.sensors;

import edu.wpi.first.wpilibj.DutyCycleEncoder;

public class REVThroughBorePWM extends DutyCycleEncoder {
    public REVThroughBorePWM(int pwmChannel) {
        super(pwmChannel);
    }
}
