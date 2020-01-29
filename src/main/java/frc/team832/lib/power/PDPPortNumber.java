package frc.team832.lib.power;

public enum PDPPortNumber {
    k0(PDPSlot.PDPBreaker.k40),
    k1(PDPSlot.PDPBreaker.k40),
    k2(PDPSlot.PDPBreaker.k40),
    k3(PDPSlot.PDPBreaker.k40),
    k4(PDPSlot.PDPBreaker.k30, PDPSlot.PDPBreaker.k20),
    k5(PDPSlot.PDPBreaker.k30, PDPSlot.PDPBreaker.k20),
    k6(PDPSlot.PDPBreaker.k30, PDPSlot.PDPBreaker.k20),
    k7(PDPSlot.PDPBreaker.k30, PDPSlot.PDPBreaker.k20),
    k8(PDPSlot.PDPBreaker.k30, PDPSlot.PDPBreaker.k20),
    k9(PDPSlot.PDPBreaker.k30, PDPSlot.PDPBreaker.k20),
    k10(PDPSlot.PDPBreaker.k30, PDPSlot.PDPBreaker.k20),
    k11(PDPSlot.PDPBreaker.k30, PDPSlot.PDPBreaker.k20),
    k12(PDPSlot.PDPBreaker.k40),
    k13(PDPSlot.PDPBreaker.k40),
    k14(PDPSlot.PDPBreaker.k40),
    k15(PDPSlot.PDPBreaker.k40);

    PDPPortNumber(PDPSlot.PDPBreaker maxBreaker, PDPSlot.PDPBreaker minBreaker) {
        this.maxBreaker = maxBreaker;
        this.minBreaker = minBreaker;
    }

    PDPPortNumber(PDPSlot.PDPBreaker maxBreaker) {
        this.maxBreaker = maxBreaker;
        this.minBreaker = maxBreaker;
    }

    public final PDPSlot.PDPBreaker maxBreaker, minBreaker;
}
