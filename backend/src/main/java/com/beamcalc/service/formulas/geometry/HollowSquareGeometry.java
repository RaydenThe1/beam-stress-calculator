package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.formulas.SectionProperties;

/**
 * Geometry adapter for a hollow square cross-section.
 */
public class HollowSquareGeometry implements Geometry {
    private final double outerSize;
    private final double wallThickness;

    public HollowSquareGeometry(CalculationRequest request) {
        this.outerSize = request.getOuterSize();
        this.wallThickness = request.getWallThickness();
    }

    @Override
    public SectionProperties computeProperties() {
        double a = outerSize;
        double inner = a - 2.0 * wallThickness;
        double A = a * a - inner * inner;
        double I = (a * a * a * a - inner * inner * inner * inner) / 12.0;
        return new SectionProperties(A, I, 0.142 * A, a / 2.0, A / (2.0 * a), wallThickness);
    }
}