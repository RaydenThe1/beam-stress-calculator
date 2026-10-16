package com.beamcalc.service.loaders;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.MaterialProperties;
import com.beamcalc.service.formulas.SectionProperties;

/**
 * Load calculator for torsional loads.
 */
public class TorsionCalculator implements LoadCalculator {

    @Override
    public LoadResult calculate(SectionProperties props, CalculationRequest request, MaterialProperties material) {
        double torque = request.getTorque();
        double length = request.getLength();
        double G = material.shearModulus;

        double tau = torque * props.maxRadius / props.polarMoment;
        double theta = torque * length / (G * props.polarMoment);
        String formula = "tau = T*r/J, theta = T*L/(G*J)";

        return new LoadResult(tau, theta, formula, "stressTorsion", "displacementTorsion");
    }
}
