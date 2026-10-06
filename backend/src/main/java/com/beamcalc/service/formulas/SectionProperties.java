package com.beamcalc.service.formulas;

public class SectionProperties {
    public final double area;
    public final double momentOfInertia;
    public final double polarMoment;
    public final double maxRadius;
    public final double firstMomentQ;
    public final double thicknessT;

    public SectionProperties(double area, double momentOfInertia, double polarMoment, double maxRadius, double firstMomentQ, double thicknessT) {
        this.area = area;
        this.momentOfInertia = momentOfInertia;
        this.polarMoment = polarMoment;
        this.maxRadius = maxRadius;
        this.firstMomentQ = firstMomentQ;
        this.thicknessT = thicknessT;
    }
}
