package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.model.CrossSection;
import com.beamcalc.service.formulas.SectionProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class IBeamGeometryTest {

    @Test
    void testIBeamProperties() {
        double h = 0.3;
        double b = 0.15;
        double tf = 0.02;
        double tw = 0.015;
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.I_SHAPED);
        req.setHeight(h);
        req.setWidth(b);
        req.setFlangeThickness(tf);
        req.setWebThickness(tw);

        Geometry geometry = new IBeamGeometry(req);
        SectionProperties props = geometry.computeProperties();

        double Af = b * tf;
        double Aw = (h - 2.0 * tf) * tw;
        double expectedArea = 2.0 * Af + Aw;
        assertThat(props.area).isCloseTo(expectedArea, within(expectedArea * 1e-6));

        double expectedR = h / 2.0;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        double expectedQ = Af * (h / 2.0 - tf / 2.0);
        assertThat(props.firstMomentQ).isCloseTo(expectedQ, within(expectedQ * 1e-6));

        assertThat(props.thicknessT).isCloseTo(tw, within(1e-9));
    }
}