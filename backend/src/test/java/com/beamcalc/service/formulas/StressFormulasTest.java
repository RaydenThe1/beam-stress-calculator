package com.beamcalc.service.formulas;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.model.CrossSection;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class StressFormulasTest {

    @Test
    void testCylinderProperties() {
        // Input: diameter = 0.1 m
        double d = 0.1;
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.CYLINDER);
        req.setOuterDiameter(d);

        SectionProperties props = StressFormulas.compute(CrossSection.CYLINDER, req);

        // Expected: Area = π*d²/4
        double expectedArea = Math.PI * d * d / 4.0;
        assertThat(props.area).isCloseTo(expectedArea, within(expectedArea * 1e-6));

        // Expected: Moment of inertia = π*d⁴/64
        double expectedI = Math.PI * d * d * d * d / 64.0;
        assertThat(props.momentOfInertia).isCloseTo(expectedI, within(expectedI * 1e-6));

        // Expected: Polar moment = π*d⁴/32
        double expectedJ = Math.PI * d * d * d * d / 32.0;
        assertThat(props.polarMoment).isCloseTo(expectedJ, within(expectedJ * 1e-6));

        // Expected: Max radius = d/2
        double expectedR = d / 2.0;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        // Expected: Q = (2/3)*r³ where r = d/2
        double r = d / 2.0;
        double expectedQ = (2.0 / 3.0) * r * r * r;
        assertThat(props.firstMomentQ).isCloseTo(expectedQ, within(expectedQ * 1e-6));

        // Expected: Thickness = d
        assertThat(props.thicknessT).isCloseTo(d, within(1e-9));
    }

    @Test
    void testDonutProperties() {
        // Input: D = 0.2 m (outer), d = 0.1 m (inner)
        double D = 0.2;
        double d = 0.1;
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.DONUT);
        req.setOuterDiameter(D);
        req.setInnerDiameter(d);

        SectionProperties props = StressFormulas.compute(CrossSection.DONUT, req);

        // Expected: Area = π(D² - d²)/4
        double expectedArea = Math.PI * (D * D - d * d) / 4.0;
        assertThat(props.area).isCloseTo(expectedArea, within(expectedArea * 1e-6));

        // Expected: Moment of inertia = π(D⁴ - d⁴)/64
        double expectedI = Math.PI * (D * D * D * D - d * d * d * d) / 64.0;
        assertThat(props.momentOfInertia).isCloseTo(expectedI, within(expectedI * 1e-6));

        // Expected: Polar moment = π(D⁴ - d⁴)/32
        double expectedJ = Math.PI * (D * D * D * D - d * d * d * d) / 32.0;
        assertThat(props.polarMoment).isCloseTo(expectedJ, within(expectedJ * 1e-6));

        // Expected: Max radius = D/2
        double expectedR = D / 2.0;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        // Expected: Thickness = D - d (wall thickness)
        assertThat(props.thicknessT).isCloseTo(D - d, within(1e-9));
    }

    @Test
    void testSquareProperties() {
        // Input: side length = 0.2 m
        double a = 0.2;
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.SQUARE);
        req.setSideLength(a);

        SectionProperties props = StressFormulas.compute(CrossSection.SQUARE, req);

        // Expected: Area = a²
        double expectedArea = a * a;
        assertThat(props.area).isCloseTo(expectedArea, within(1e-9));

        // Expected: Moment of inertia = a⁴/12
        double expectedI = a * a * a * a / 12.0;
        assertThat(props.momentOfInertia).isCloseTo(expectedI, within(expectedI * 1e-6));

        // Expected: Polar moment ≈ 0.142 * a⁴
        double expectedJ = 0.142 * a * a * a * a;
        assertThat(props.polarMoment).isCloseTo(expectedJ, within(expectedJ * 1e-3)); // St. Venant is approximate

        // Expected: Max radius = a/2
        double expectedR = a / 2.0;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        // Expected: Q = A/(2*a) = a²/(2*a) = a/2
        double expectedQ = a / 2.0;
        assertThat(props.firstMomentQ).isCloseTo(expectedQ, within(1e-9));

        // Expected: Thickness = a
        assertThat(props.thicknessT).isCloseTo(a, within(1e-9));
    }

    @Test
    void testHollowSquareProperties() {
        // Input: outer = 0.3 m, wall thickness = 0.02 m
        double outerSize = 0.3;
        double wallThickness = 0.02;
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.HOLLOW_SQUARE);
        req.setOuterSize(outerSize);
        req.setWallThickness(wallThickness);

        SectionProperties props = StressFormulas.compute(CrossSection.HOLLOW_SQUARE, req);

        // Expected: Inner size
        double innerSize = outerSize - 2.0 * wallThickness;

        // Expected: Area = outerSize² - innerSize²
        double expectedArea = outerSize * outerSize - innerSize * innerSize;
        assertThat(props.area).isCloseTo(expectedArea, within(1e-9));

        // Expected: Moment of inertia = (outerSize⁴ - innerSize⁴)/12
        double expectedI = (outerSize * outerSize * outerSize * outerSize - innerSize * innerSize * innerSize * innerSize) / 12.0;
        assertThat(props.momentOfInertia).isCloseTo(expectedI, within(expectedI * 1e-6));

        // Expected: Polar moment ≈ 0.142 * (outerSize⁴ - innerSize⁴)
        double expectedJ = 0.142 * (outerSize * outerSize * outerSize * outerSize - innerSize * innerSize * innerSize * innerSize);
        assertThat(props.polarMoment).isCloseTo(expectedJ, within(expectedJ * 1e-3));

        // Expected: Max radius = outerSize/2
        double expectedR = outerSize / 2.0;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        // Expected: Thickness = wallThickness
        assertThat(props.thicknessT).isCloseTo(wallThickness, within(1e-9));
    }

    @Test
    void testIBeamProperties() {
        // Input: h=0.3, b=0.15, tf=0.02, tw=0.015
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

        SectionProperties props = StressFormulas.compute(CrossSection.I_SHAPED, req);

        // Expected: Area = 2*Af + Aw
        double Af = b * tf;
        double Aw = (h - 2.0 * tf) * tw;
        double expectedArea = 2.0 * Af + Aw;
        assertThat(props.area).isCloseTo(expectedArea, within(expectedArea * 1e-6));

        // Expected: Centroid on neutral axis (h/2)
        double expectedR = h / 2.0;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        // Expected: Q = Af * (h/2 - tf/2) (first moment at web-flange junction)
        double expectedQ = Af * (h / 2.0 - tf / 2.0);
        assertThat(props.firstMomentQ).isCloseTo(expectedQ, within(expectedQ * 1e-6));

        // Expected: Thickness = tw (web thickness)
        assertThat(props.thicknessT).isCloseTo(tw, within(1e-9));
    }

    @Test
    void testTBeamProperties() {
        // Input: h=0.3, b=0.15, tf=0.02, tw=0.015
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

        SectionProperties props = StressFormulas.compute(CrossSection.T_SHAPED, req);

        // Expected: Area = flangeA + webA
        double flangeA = b * tf;
        double webA = (h - tf) * tw;
        double expectedArea = flangeA + webA;
        assertThat(props.area).isCloseTo(expectedArea, within(expectedArea * 1e-6));

        // Expected: Centroid calculation
        double yc = (flangeA * (h - tf / 2.0) + webA * (h - tf) / 2.0) / expectedArea;

        // Expected: Moment of inertia (with corrected formula for If and Iw)
        double If = b * tf * tf * tf / 12.0;
        double Iw = tw * (h - tf) * (h - tf) * (h - tf) / 12.0;
        double expectedI = If + flangeA * Math.pow((h - tf / 2.0) - yc, 2)
                         + Iw + webA * Math.pow((h - tf) / 2.0 - yc, 2);
        assertThat(props.momentOfInertia).isCloseTo(expectedI, within(expectedI * 1e-6));

        // Expected: Max radius = h - yc
        double expectedR = h - yc;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        // Expected: Q = flangeA * ((h - tf/2) - yc)
        double expectedQ = flangeA * ((h - tf / 2.0) - yc);
        assertThat(props.firstMomentQ).isCloseTo(expectedQ, within(expectedQ * 1e-6));

        // Expected: Thickness = tw
        assertThat(props.thicknessT).isCloseTo(tw, within(1e-9));
    }
}
