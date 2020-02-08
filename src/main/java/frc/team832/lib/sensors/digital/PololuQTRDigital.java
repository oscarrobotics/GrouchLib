package frc.team832.lib.sensors.digital;

public class PololuQTRDigital {

    private static final int kDefaultTimeoutNanos = 2500000;

    private final DigitalPin pin;

    private int timeout = kDefaultTimeoutNanos;
    long value;

    public PololuQTRDigital(int dioChannel) {
        pin = new DigitalPin(dioChannel);
        pin.setDirection(true);
    }

    public void setTimeoutMicros(int micros) {
        timeout = micros * 1000;
    }

    public long getValue() {
        readPrivate();
        return value;
    }

    private void readPrivate() {
        pin.setDirection(false);
        pin.set(true);

        // TODO: FIX!!
        try {
            Thread.sleep(0, 7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long startTime = System.nanoTime();
        long time = 0;

        pin.setDirection(true);

        while (time < timeout) {

            time = System.nanoTime() - startTime;

            if ((pin.get() && time < value)) {
                value = time;
            }
        }
    }
}
