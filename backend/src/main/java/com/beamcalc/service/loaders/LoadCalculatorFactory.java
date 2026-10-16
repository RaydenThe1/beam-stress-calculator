package com.beamcalc.service.loaders;

import com.beamcalc.model.LoadType;
import org.springframework.stereotype.Component;

/**
 * Factory for creating LoadCalculator adapters based on load type.
 * Spring-managed component for dependency injection.
 */
@Component
public class LoadCalculatorFactory {

    /**
     * Creates the appropriate LoadCalculator adapter for the given load type.
     *
     * @param type The load type from the calculation request
     * @return LoadCalculator adapter for the specified load type
     * @throws IllegalArgumentException if load type is not supported
     */
    public LoadCalculator create(LoadType type) {
        return switch (type) {
            case AXIAL -> new AxialLoadCalculator();
            case TORSION -> new TorsionCalculator();
            case SHEAR -> new ShearCalculator();
        };
    }
}