package com.beamcalc.service.loaders;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.MaterialProperties;
import com.beamcalc.service.formulas.SectionProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class TorsionCalculatorTest {

    @Test
    void testTorsionCalculation() {
        SectionProperties props = new SectionProperties(0.01, 8.33e-6, 1.67e-5, 0.05, 0.000333, 0.1);
        CalculationRequest req = new CalculationRequest();
        req.setTorque(1000.0);
        req.setLength(1.0);
        MaterialProperties mat = MaterialProperties.STEEL;

        LoadCalculator calculator = new TorsionCalculator();
        LoadResult result = calculator.calculate(props, req, mat);

        double expectedTau = 1000.0 * 0.05 / 1.67e-5;
        double expectedTheta = 1000.0 * 1.0 / (79.4e9 * 1.67e-5);

        assertThat(result.getStress()).isCloseTo(expectedTau, within(Math.abs(expectedTau) * 1e-6));
        assertThat(result.getDisplacement()).isCloseTo(expectedTheta, within(Math.abs(expectedTheta) * 1e-6));
        assertThat(result.getFormulaUsed()).isEqualTo("tau = T*r/J, theta = T*L/(G*J)");
        assertThat(result.getStressLabel()).isEqualTo("stressTorsion");
    }
}