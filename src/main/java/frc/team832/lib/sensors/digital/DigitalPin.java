// package frc.team832.lib.sensors.digital;

// import edu.wpi.first.hal.DIOJNI;
// import edu.wpi.first.hal.FRCNetComm;
// import edu.wpi.first.hal.HAL;
// import edu.wpi.first.hal.SimDevice;
// import edu.wpi.first.util.sendable.Sendable;
// import edu.wpi.first.util.sendable.SendableRegistry;
// import edu.wpi.first.wpilibj.DigitalSource;
// import edu.wpi.first.wpilibj.SensorUtil;


// public class DigitalPin extends DigitalSource implements Sendable {

//     private final int m_channel;
//     private int m_handle;

//     private boolean isInput = true;

//     /**
//      * Create an instance of a Digital Input class. Creates a digital input given a channel.
//      *
//      * @param channel the DIO channel for the digital input 0-9 are on-board, 10-25 are on the MXP
//      */
//     public DigitalPin(int channel) {
//         SensorUtil.checkDigitalChannel(channel);
//         this.m_channel = channel;

//         m_handle = DIOJNI.initializeDIOPort(HAL.getPort((byte) channel), true);

// //        HAL.report(FRCNetComm.tResourceType.kResourceType_DigitalInput, channel + 1);
// //        SendableRegistry.addLW(this, "DigitalInput", channel);
//     }

//     @Override
//     public void close() {
//         super.close();
//         SendableRegistry.remove(this);
//         if (m_interrupt != 0) {
//             cancelInterrupts();
//         }
//         DIOJNI.freeDIOPort(m_handle);
//         m_handle = 0;
//     }

//     public void setDirection(boolean isInput) {
//         this.isInput = isInput;
//         DIOJNI.setDIODirection(m_handle, isInput);
//     }

//     /**
//      * Set the value of a digital output.
//      *
//      * @param value true is on, off is false
//      */
//     public void set(boolean value) {
//         if (!isInput) {
//             DIOJNI.setDIO(m_handle, (short) (value ? 1 : 0));
//         }
//     }

//     /**
//      * Gets the value being output from the Digital Output.
//      *
//      * @return the state of the digital output.
//      */
//     public boolean get() {
//         return DIOJNI.getDIO(m_handle);
//     }


//     /**
//      * Get the GPIO channel number that this object represents.
//      *
//      * @return The GPIO channel number.
//      */
//     @Override
//     public int getChannel() {
//         return m_channel;
//     }

//     /**
//      * Is this an analog trigger.
//      *
//      * @return true if this is an analog trigger
//      */
//     @Override
//     public boolean isAnalogTrigger() {
//         return false;
//     }

//     /**
//      * Generate a single pulse. There can only be a single pulse going at any time.
//      *
//      * @param pulseLength The length of the pulse.
//      */
//     public void pulse(final double pulseLength) {
//         DIOJNI.pulse(m_handle, pulseLength);
//     }

//     /**
//      * Determine if the pulse is still going. Determine if a previously started pulse is still going.
//      *
//      * @return true if pulsing
//      */
//     public boolean isPulsing() {
//         return DIOJNI.isPulsing(m_handle);
//     }

//     /**
//      * Indicates this input is used by a simulated device.
//      *
//      * @param device simulated device handle
//      */
//     public void setSimDevice(SimDevice device) {
//         DIOJNI.setDIOSimDevice(m_handle, device.getNativeHandle());
//     }

//     @Override
//     public int getAnalogTriggerTypeForRouting() {
//         return 0;
//     }

//     @Override
//     public int getPortHandleForRouting() {
//         return 0;
//     }

//     @Override
//     public void initSendable(SendableBuilder builder) {
//         builder.setSmartDashboardType("Digital Input");
//         builder.addBooleanProperty("Value", this::get, null);
//     }
// }
