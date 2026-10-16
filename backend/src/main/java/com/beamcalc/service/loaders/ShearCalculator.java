package com.beamcalc.service.loaders;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.MaterialProperties;
import com.beamcalc.service.formulas.SectionProperties;

/**
 * Load calculator for shear loads.
 */
public class ShearCalculator implements LoadCalculator {

    @Override
    public LoadResult calculate(SectionProperties props, CalculationRequest request, MaterialProperties material) {
        double shearForce = request.getShearForce();
        double length = request.getLength();
        double E = material.youngsModulus;

        double tau = shearForce * props.firstMomentQ / (props.momentOfInertia * props.thicknessT);
        double delta = shearForce * length * length * length / (48.0 * E * props.momentOfInertia);
        String formula = "tau = V*Q/(I*t), delta = V*L^3/(48*E*I)";

        return new LoadResult(tau, delta, formula, "stressShear", "displacementShear");
    }
}
