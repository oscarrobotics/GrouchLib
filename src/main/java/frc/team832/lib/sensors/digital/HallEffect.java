package frc.team832.lib.sensors.digital;

import edu.wpi.first.wpilibj.DigitalInput;

import java.util.function.Consumer;
import java.util.function.Function;

public class HallEffect extends DigitalInput {

    /**
     * Create an instance of a HallEffect class. Creates a hall effect given a channel.
     *
     * @param channel the DIO channel for the hall effect 0-9 are on-board, 10-25 are on the MXP
     */
    public HallEffect(int channel) {
        super(channel);
    }

    public void setupInterrupts(Runnable callback) {
        this.requestInterrupts((WaitResult result) -> {
            if (WaitResult.getValue(true, false).equals(result)) {
                callback.run();
            }
        });
        enableInterrupts();
    }
}
