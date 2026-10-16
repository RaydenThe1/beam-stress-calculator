package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.formulas.SectionProperties;

/**
 * Geometry adapter for a solid circular cross-section (cylinder).
 */
public class CylinderGeometry implements Geometry {
    private final double diameter;

    public CylinderGeometry(CalculationRequest request) {
        this.diameter = request.getOuterDiameter();
    }

    @Override
    public SectionProperties computeProperties() {
        double d = diameter;
        double a = Math.PI * d * d / 4.0;
        double I = Math.PI * d * d * d * d / 64.0;
        double J = Math.PI * d * d * d * d / 32.0;
        double r = d / 2.0;
        return new SectionProperties(a, I, J, r, 4.0 * a / (3.0 * Math.PI * r), d);
    }
}