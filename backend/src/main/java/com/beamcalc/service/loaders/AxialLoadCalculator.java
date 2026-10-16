package com.beamcalc.service.loaders;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.MaterialProperties;
import com.beamcalc.service.formulas.SectionProperties;

/**
 * Load calculator for axial loads (tension/compression).
 */
public class AxialLoadCalculator implements LoadCalculator {

    @Override
    public LoadResult calculate(SectionProperties props, CalculationRequest request, MaterialProperties material) {
        double force = request.getAxialForce();
        double length = request.getLength();
        double E = material.youngsModulus;

        double stress = force / props.area;
        double displacement = force * length / (props.area * E);
        String formula = "sigma = F/A, delta = F*L/(A*E)";

        return new LoadResult(stress, displacement, formula,
                            "stressTractionCompression", "displacementTractionCompression");
    }
}
