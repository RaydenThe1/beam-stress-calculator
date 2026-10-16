package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.formulas.SectionProperties;

/**
 * Geometry adapter for a solid square cross-section.
 */
public class SquareGeometry implements Geometry {
    private final double sideLength;

    public SquareGeometry(CalculationRequest request) {
        this.sideLength = request.getSideLength();
    }

    @Override
    public SectionProperties computeProperties() {
        double a = sideLength;
        double A = a * a;
        double I = a * a * a * a / 12.0;
        double J = 0.142 * a * a * a * a; // St. Venant torsional constant
        return new SectionProperties(A, I, J, a / 2.0, A / (2.0 * a), a);
    }
}