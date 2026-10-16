package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.model.CrossSection;
import com.beamcalc.service.formulas.SectionProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class TBeamGeometryTest {

    @Test
    void testTBeamProperties() {
        double h = 0.3;
        double b = 0.15;
        double tf = 0.02;
        double tw = 0.015;
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.T_SHAPED);
        req.setHeight(h);
        req.setWidth(b);
        req.setFlangeThickness(tf);
        req.setWebThickness(tw);

        Geometry geometry = new TBeamGeometry(req);
        SectionProperties props = geometry.computeProperties();

        double flangeA = b * tf;
        double webA = (h - tf) * tw;
        double expectedArea = flangeA + webA;
        assertThat(props.area).isCloseTo(expectedArea, within(expectedArea * 1e-6));

        double yc = (flangeA * (h - tf / 2.0) + webA * (h - tf) / 2.0) / expectedArea;

        double If = b * tf * tf * tf / 12.0;
        double Iw = tw * (h - tf) * (h - tf) * (h - tf) / 12.0;
        double expectedI = If + flangeA * Math.pow((h - tf / 2.0) - yc, 2)
                         + Iw + webA * Math.pow((h - tf) / 2.0 - yc, 2);
        assertThat(props.momentOfInertia).isCloseTo(expectedI, within(expectedI * 1e-6));

        double expectedR = h - yc;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        double expectedQ = flangeA * ((h - tf / 2.0) - yc);
        assertThat(props.firstMomentQ).isCloseTo(expectedQ, within(expectedQ * 1e-6));

        assertThat(props.thicknessT).isCloseTo(tw, within(1e-9));
    }
}