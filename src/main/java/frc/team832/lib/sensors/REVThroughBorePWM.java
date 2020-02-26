package frc.team832.lib.sensors;

import edu.wpi.first.wpilibj2.DutyCycleEncoder;

public class REVThroughBorePWM extends DutyCycleEncoder {
    public REVThroughBorePWM(int pwmChannel) {
        super(pwmChannel);
    }
}
