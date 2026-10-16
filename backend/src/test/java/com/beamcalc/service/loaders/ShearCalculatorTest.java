package com.beamcalc.service.loaders;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.MaterialProperties;
import com.beamcalc.service.formulas.SectionProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class ShearCalculatorTest {

    @Test
    void testShearCalculation() {
        SectionProperties props = new SectionProperties(0.01, 8.33e-6, 1.67e-5, 0.05, 0.000333, 0.1);
        CalculationRequest req = new CalculationRequest();
        req.setShearForce(5000.0);
        req.setLength(1.0);
        MaterialProperties mat = MaterialProperties.STEEL;

        LoadCalculator calculator = new ShearCalculator();
        LoadResult result = calculator.calculate(props, req, mat);

        double expectedTau = 5000.0 * 0.000333 / (8.33e-6 * 0.1);
        double expectedDelta = 5000.0 * 1.0 * 1.0 * 1.0 / (48.0 * 200e9 * 8.33e-6);

        assertThat(result.getStress()).isCloseTo(expectedTau, within(Math.abs(expectedTau) * 1e-6));
        assertThat(result.getDisplacement()).isCloseTo(expectedDelta, within(Math.abs(expectedDelta) * 1e-6));
        assertThat(result.getFormulaUsed()).isEqualTo("tau = V*Q/(I*t), delta = V*L^3/(48*E*I)");
        assertThat(result.getStressLabel()).isEqualTo("stressShear");
    }
}