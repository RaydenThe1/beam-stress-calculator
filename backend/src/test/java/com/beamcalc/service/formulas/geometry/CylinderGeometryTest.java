package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.model.CrossSection;
import com.beamcalc.service.formulas.SectionProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class CylinderGeometryTest {

    @Test
    void testCylinderProperties() {
        double d = 0.1;
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.CYLINDER);
        req.setOuterDiameter(d);

        Geometry geometry = new CylinderGeometry(req);
        SectionProperties props = geometry.computeProperties();

        double expectedArea = Math.PI * d * d / 4.0;
        assertThat(props.area).isCloseTo(expectedArea, within(expectedArea * 1e-6));

        double expectedI = Math.PI * d * d * d * d / 64.0;
        assertThat(props.momentOfInertia).isCloseTo(expectedI, within(expectedI * 1e-6));

        double expectedJ = Math.PI * d * d * d * d / 32.0;
        assertThat(props.polarMoment).isCloseTo(expectedJ, within(expectedJ * 1e-6));

        double expectedR = d / 2.0;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        double r = d / 2.0;
        double expectedQ = (2.0 / 3.0) * r * r * r;
        assertThat(props.firstMomentQ).isCloseTo(expectedQ, within(expectedQ * 1e-6));

        assertThat(props.thicknessT).isCloseTo(d, within(1e-9));
    }
}