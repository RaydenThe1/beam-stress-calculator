package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.formulas.SectionProperties;

/**
 * Geometry adapter for a T-beam cross-section.
 */
public class TBeamGeometry implements Geometry {
    private final double height;
    private final double width;
    private final double flangeThickness;
    private final double webThickness;

    public TBeamGeometry(CalculationRequest request) {
        this.height = request.getHeight();
        this.width = request.getWidth();
        this.flangeThickness = request.getFlangeThickness();
        this.webThickness = request.getWebThickness();
    }

    @Override
    public SectionProperties computeProperties() {
        double h = height;
        double b = width;
        double tf = flangeThickness;
        double tw = webThickness;

        double flangeA = b * tf;
        double webA = (h - tf) * tw;
        double A = flangeA + webA;
        double yc = (flangeA * (h - tf / 2.0) + webA * (h - tf) / 2.0) / A;
        double If = b * b * b * tf / 12.0;
        double Iw = tw * tw * (h - tf) / 12.0;
        double I = If + flangeA * Math.pow((h - tf / 2.0) - yc, 2) + Iw + webA * Math.pow((h - tf) / 2.0 - yc, 2);
        double J = 0.142 * h * h * h * h; // approx
        double Q = flangeA * ((h - tf / 2.0) - yc);

        return new SectionProperties(A, I, J, h - yc, Q, tw);
    }
}