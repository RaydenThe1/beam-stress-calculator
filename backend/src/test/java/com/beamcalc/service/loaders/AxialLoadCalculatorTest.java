package com.beamcalc.service.loaders;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.service.MaterialProperties;
import com.beamcalc.service.formulas.SectionProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class AxialLoadCalculatorTest {

    @Test
    void testAxialLoadCalculation() {
        SectionProperties props = new SectionProperties(0.01, 8.33e-6, 1.67e-5, 0.05, 0.000333, 0.1);
        CalculationRequest req = new CalculationRequest();
        req.setAxialForce(10000.0);
        req.setLength(1.0);
        MaterialProperties mat = MaterialProperties.STEEL;

        LoadCalculator calculator = new AxialLoadCalculator();
        LoadResult result = calculator.calculate(props, req, mat);

        double expectedStress = 10000.0 / 0.01;
        double expectedDisp = 10000.0 * 1.0 / (0.01 * 200e9);

        assertThat(result.getStress()).isCloseTo(expectedStress, within(expectedStress * 1e-6));
        assertThat(result.getDisplacement()).isCloseTo(expectedDisp, within(expectedDisp * 1e-6));
        assertThat(result.getFormulaUsed()).isEqualTo("sigma = F/A, delta = F*L/(A*E)");
        assertThat(result.getStressLabel()).isEqualTo("stressTractionCompression");
    }
}