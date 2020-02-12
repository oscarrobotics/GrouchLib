package frc.team832.lib.driverinput.controllers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

@SuppressWarnings({"unused", "WeakerAccess"})
public class StratComInterface extends GenericHID {

    //Constructor
    public StratComInterface(int port) {
        super(port);
    }

    //Represent buttons on StratComInterface
    public enum SCIButton {
        SC1(1),
        SC2(2),
        SC3(3),
        SC4(4),
        SC5(5),
        SC6(6),
        SCPlus(7),
        SCMinus(8),
        SCSideTop(9),
        SCSideMid(10),
        SCSideBot(11),
        ArcadeBlackLeft(12),
        ArcadeBlackRight(13),
        ArcadeWhiteLeft(14),
        ArcadeWhiteRight(15),
        SingleToggle(16),
        DoubleToggleUp(17),
        DoubleToggleDown(18),
        KeySwitch(19);

        public final int index;

        SCIButton(int value) {
            this.index = value;
        }
    }

    private final JoystickButton SC1 = new JoystickButton(this, SCIButton.SC1.index);
    private final JoystickButton SC2 = new JoystickButton(this, SCIButton.SC2.index);
    private final JoystickButton SC3 = new JoystickButton(this, SCIButton.SC3.index);
    private final JoystickButton SC4 = new JoystickButton(this, SCIButton.SC4.index);
    private final JoystickButton SC5 = new JoystickButton(this, SCIButton.SC5.index);
    private final JoystickButton SC6 = new JoystickButton(this, SCIButton.SC6.index);
    private final JoystickButton SCPlus = new JoystickButton(this, SCIButton.SCPlus.index);
    private final JoystickButton SCMinus = new JoystickButton(this, SCIButton.SCMinus.index);
    private final JoystickButton SCSideTop = new JoystickButton(this, SCIButton.SCSideTop.index);
    private final JoystickButton SCSideMid = new JoystickButton(this, SCIButton.SCSideMid.index);
    private final JoystickButton SCSideBot = new JoystickButton(this, SCIButton.SCSideBot.index);
    private final JoystickButton ArcadeBlackLeft = new JoystickButton(this, SCIButton.ArcadeBlackLeft.index);
    private final JoystickButton ArcadeBlackRight = new JoystickButton(this, SCIButton.ArcadeBlackRight.index);
    private final JoystickButton ArcadeWhiteLeft = new JoystickButton(this, SCIButton.ArcadeWhiteLeft.index);
    private final JoystickButton ArcadeWhiteRight = new JoystickButton(this, SCIButton.ArcadeWhiteRight.index);
    private final JoystickButton SingleToggle = new JoystickButton(this, SCIButton.SingleToggle.index);
    private final JoystickButton DoubleToggleUp = new JoystickButton(this, SCIButton.DoubleToggleUp.index);
    private final JoystickButton DoubleToggleDown = new JoystickButton(this, SCIButton.DoubleToggleDown.index);
    private final JoystickButton KeySwitch = new JoystickButton(this, SCIButton.KeySwitch.index);

    public enum ThreeSwitchPos {
        SWITCH_UP,
        SWITCH_DOWN,
        SWITCH_OFF
    }

    public ThreeSwitchPos getThreeSwitch () {
        if (getDoubleToggleUp().get()) {
            return ThreeSwitchPos.SWITCH_UP;
        } else if (getDoubleToggleDown().get()) {
            return ThreeSwitchPos.SWITCH_DOWN;
        }
        return ThreeSwitchPos.SWITCH_OFF;
    }

    //Gets current state of key switch
    public JoystickButton getKeySwitch() { return KeySwitch;}

    //Gets current states for each of the main SCI buttons (the ones with the numbers)
    public JoystickButton getSC1() { return SC1;}
    public JoystickButton getSC2() { return SC2;}
    public JoystickButton getSC3() { return SC3;}
    public JoystickButton getSC4() { return SC4;}
    public JoystickButton getSC5() { return SC5;}
    public JoystickButton getSC6() { return SC6;}

    //Gets current state of plus and minus buttons
    public JoystickButton getSCPlus() { return SCPlus;}
    public JoystickButton getSCMinus() { return SCMinus;}

    //Gets current state of SCI side buttons
    public JoystickButton getSCSideTop() { return SCSideTop;}
    public JoystickButton getSCSideMid() { return SCSideMid;}
    public JoystickButton getSCSideBot() { return SCSideBot;}

    //Gets current state of the 4 arcade buttons
    public JoystickButton getArcadeBlackLeft() { return ArcadeBlackLeft;}
    public JoystickButton getArcadeBlackRight() { return ArcadeBlackRight;}
    public JoystickButton getArcadeWhiteLeft() { return ArcadeWhiteLeft;}
    public JoystickButton getArcadeWhiteRight() { return ArcadeWhiteRight;}

    //Gets state of single toggle
    public JoystickButton getSingleToggle() { return SingleToggle;}

    //Gets state of double toggle
    public JoystickButton getDoubleToggleUp() { return DoubleToggleUp;}
    public JoystickButton getDoubleToggleDown() { return DoubleToggleDown;}

    //Gets state of left slider
    @Override
    public double getX(Hand hand) {
        return getRawAxis(0);
    }
    //Gets state of right slider
    @Override
    public double getY(Hand hand) { return getRawAxis(1); }

    public double getLeftSlider() { return getX(); }

    public double getRightSlider() {
        return getY();
    }
}