package com.beamcalc.service.loaders;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.MaterialProperties;
import com.beamcalc.service.formulas.SectionProperties;

/**
 * Interface for load type calculators.
 * Provides a seam for polymorphic load calculation strategies.
 */
public interface LoadCalculator {
    /**
     * Calculates stress and displacement for this load type.
     *
     * @param props Section properties from geometry calculation
     * @param request The calculation request containing load parameters
     * @param material Material properties (Young's modulus, shear modulus)
     * @return LoadResult containing stress, displacement, and formula used
     */
    LoadResult calculate(SectionProperties props, CalculationRequest request, MaterialProperties material);
}
