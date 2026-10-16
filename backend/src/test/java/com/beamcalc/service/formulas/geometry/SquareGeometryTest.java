package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.model.CrossSection;
import com.beamcalc.service.formulas.SectionProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class SquareGeometryTest {

    @Test
    void testSquareProperties() {
        double a = 0.2;
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.SQUARE);
        req.setSideLength(a);

        Geometry geometry = new SquareGeometry(req);
        SectionProperties props = geometry.computeProperties();

        double expectedArea = a * a;
        assertThat(props.area).isCloseTo(expectedArea, within(1e-9));

        double expectedI = a * a * a * a / 12.0;
        assertThat(props.momentOfInertia).isCloseTo(expectedI, within(expectedI * 1e-6));

        double expectedJ = 0.142 * a * a * a * a;
        assertThat(props.polarMoment).isCloseTo(expectedJ, within(expectedJ * 1e-3));

        double expectedR = a / 2.0;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        double expectedQ = a / 2.0;
        assertThat(props.firstMomentQ).isCloseTo(expectedQ, within(1e-9));

        assertThat(props.thicknessT).isCloseTo(a, within(1e-9));
    }
}