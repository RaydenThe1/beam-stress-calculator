package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.formulas.SectionProperties;

/**
 * Geometry adapter for an I-beam cross-section.
 */
public class IBeamGeometry implements Geometry {
    private final double height;
    private final double width;
    private final double flangeThickness;
    private final double webThickness;

    public IBeamGeometry(CalculationRequest request) {
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

        double Af = b * tf;
        double Aw = (h - 2.0 * tf) * tw;
        double A = 2.0 * Af + Aw;
        double I = (b * h * h * h / 12.0) - ((b - tw) * (h - 2.0 * tf) * (h - 2.0 * tf) * (h - 2.0 * tf) / 12.0);
        double J = 0.142 * h * h * h * h; // approx for thin-walled I
        double Q = Af * (h / 2.0 - tf / 2.0);

        return new SectionProperties(A, I, J, h / 2.0, Q, tw);
    }
}