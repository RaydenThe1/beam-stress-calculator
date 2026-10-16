package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.model.CrossSection;
import com.beamcalc.service.formulas.SectionProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class DonutGeometryTest {

    @Test
    void testDonutProperties() {
        double D = 0.2;
        double d = 0.1;
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.DONUT);
        req.setOuterDiameter(D);
        req.setInnerDiameter(d);

        Geometry geometry = new DonutGeometry(req);
        SectionProperties props = geometry.computeProperties();

        double expectedArea = Math.PI * (D * D - d * d) / 4.0;
        assertThat(props.area).isCloseTo(expectedArea, within(expectedArea * 1e-6));

        double expectedI = Math.PI * (D * D * D * D - d * d * d * d) / 64.0;
        assertThat(props.momentOfInertia).isCloseTo(expectedI, within(expectedI * 1e-6));

        double expectedJ = Math.PI * (D * D * D * D - d * d * d * d) / 32.0;
        assertThat(props.polarMoment).isCloseTo(expectedJ, within(expectedJ * 1e-6));

        double expectedR = D / 2.0;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        assertThat(props.thicknessT).isCloseTo(D - d, within(1e-9));
    }
}
