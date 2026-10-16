package com.beamcalc.service.formulas.geometry;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.model.CrossSection;
import com.beamcalc.service.formulas.SectionProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class HollowSquareGeometryTest {

    @Test
    void testHollowSquareProperties() {
        double outerSize = 0.3;
        double wallThickness = 0.02;
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.HOLLOW_SQUARE);
        req.setOuterSize(outerSize);
        req.setWallThickness(wallThickness);

        Geometry geometry = new HollowSquareGeometry(req);
        SectionProperties props = geometry.computeProperties();

        double innerSize = outerSize - 2.0 * wallThickness;
        double expectedArea = outerSize * outerSize - innerSize * innerSize;
        assertThat(props.area).isCloseTo(expectedArea, within(1e-9));

        double expectedI = (outerSize * outerSize * outerSize * outerSize - innerSize * innerSize * innerSize * innerSize) / 12.0;
        assertThat(props.momentOfInertia).isCloseTo(expectedI, within(expectedI * 1e-6));

        double expectedR = outerSize / 2.0;
        assertThat(props.maxRadius).isCloseTo(expectedR, within(1e-9));

        assertThat(props.thicknessT).isCloseTo(wallThickness, within(1e-9));
    }
}