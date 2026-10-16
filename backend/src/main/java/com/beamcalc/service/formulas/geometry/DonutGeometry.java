package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.formulas.SectionProperties;

/**
 * Geometry adapter for a hollow circular cross-section (donut/tube).
 */
public class DonutGeometry implements Geometry {
    private final double outerDiameter;
    private final double innerDiameter;

    public DonutGeometry(CalculationRequest request) {
        this.outerDiameter = request.getOuterDiameter();
        this.innerDiameter = request.getInnerDiameter();
    }

    @Override
    public SectionProperties computeProperties() {
        double D = outerDiameter;
        double d = innerDiameter;
        double a = Math.PI * (D * D - d * d) / 4.0;
        double I = Math.PI * (D * D * D * D - d * d * d * d) / 64.0;
        double J = Math.PI * (D * D * D * D - d * d * d * d) / 32.0;
        double r = D / 2.0;
        return new SectionProperties(a, I, J, r, 4.0 * a / (3.0 * Math.PI * r), D - d);
    }
}