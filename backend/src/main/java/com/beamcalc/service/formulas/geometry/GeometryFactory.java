package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.model.CrossSection;
import org.springframework.stereotype.Component;

/**
 * Factory for creating Geometry adapters based on cross-section type.
 * Spring-managed component for dependency injection.
 */
@Component
public class GeometryFactory {

    /**
     * Creates the appropriate Geometry adapter for the given cross-section type.
     *
     * @param type The cross-section type from the calculation request
     * @param request The calculation request containing geometry parameters
     * @return Geometry adapter for the specified cross-section
     * @throws IllegalArgumentException if cross-section type is not supported
     */
    public Geometry create(CrossSection type, CalculationRequest request) {
        return switch (type) {
            case CYLINDER -> new CylinderGeometry(request);
            case DONUT -> new DonutGeometry(request);
            case SQUARE -> new SquareGeometry(request);
            case HOLLOW_SQUARE -> new HollowSquareGeometry(request);
            case I_SHAPED -> new IBeamGeometry(request);
            case T_SHAPED -> new TBeamGeometry(request);
        };
    }
}