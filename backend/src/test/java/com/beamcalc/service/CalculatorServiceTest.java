package com.beamcalc.service;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.api.dto.CalculationResponse;
import com.beamcalc.model.CrossSection;
import com.beamcalc.model.LoadType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class CalculatorServiceTest {

    private final CalculatorService service = new CalculatorService();

    @Test
    void testAxialLoadCylinder() {
        // Setup: Cylinder with d=0.1m, axial force=1000N, length=1m
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.CYLINDER);
        req.setLoadType(LoadType.AXIAL);
        req.setLength(1.0);
        req.setOuterDiameter(0.1);
        req.setAxialForce(1000);

        CalculationResponse resp = service.calculate(req);

        // Expected: Area = π*(0.1)²/4 ≈ 0.00785398
        double area = Math.PI * 0.01 / 4.0;

        // Expected: Stress = F/A = 1000 / area
        double expectedStress = 1000.0 / area;
        assertThat(resp.getStressTractionCompression())
            .isCloseTo(expectedStress, within(expectedStress * 1e-6));

        // Expected: Displacement = F*L/(A*E) = 1000 * 1 / (area * 200e9)
        double expectedDisp = 1000.0 * 1.0 / (area * 200e9);
        assertThat(resp.getDisplacementTractionCompression())
            .isCloseTo(expectedDisp, within(expectedDisp * 1e-6 + 1e-15));

        // Verify formula string is set
        assertThat(resp.getFormulaUsed()).contains("sigma = F/A");

        // Verify section properties are populated
        assertThat(resp.getSectionProperties()).isNotNull();
        assertThat(resp.getSectionProperties().getArea()).isCloseTo(area, within(area * 1e-6));
    }

    @Test
    void testAxialLoadWithCustomYoungsModulus() {
        // Setup: Same as above but with custom E
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.CYLINDER);
        req.setLoadType(LoadType.AXIAL);
        req.setLength(1.0);
        req.setOuterDiameter(0.1);
        req.setAxialForce(1000);
        req.setYoungsModulus(210e9); // Custom E (higher than default 200e9)

        CalculationResponse resp = service.calculate(req);

        double area = Math.PI * 0.01 / 4.0;

        // Expected: Displacement with E=210e9 (smaller than default)
        double expectedDisp = 1000.0 * 1.0 / (area * 210e9);
        assertThat(resp.getDisplacementTractionCompression())
            .isCloseTo(expectedDisp, within(expectedDisp * 1e-6 + 1e-15));

        // Verify it differs from default steel E displacement
        double defaultDisp = 1000.0 * 1.0 / (area * 200e9);
        assertThat(resp.getDisplacementTractionCompression())
            .isNotCloseTo(defaultDisp, within(defaultDisp * 1e-3));
    }

    @Test
    void testTorsionLoadDonut() {
        // Setup: Donut (hollow cylinder) D=0.2, d=0.1, torque=200, length=0.5
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.DONUT);
        req.setLoadType(LoadType.TORSION);
        req.setLength(0.5);
        req.setOuterDiameter(0.2);
        req.setInnerDiameter(0.1);
        req.setTorque(200);

        CalculationResponse resp = service.calculate(req);

        // Expected: Polar moment J = π(D⁴ - d⁴)/32
        double J = Math.PI * (Math.pow(0.2, 4) - Math.pow(0.1, 4)) / 32.0;
        double maxRadius = 0.1; // D/2

        // Expected: Shear stress τ = T*r/J
        double expectedTau = 200.0 * maxRadius / J;
        assertThat(resp.getStressTorsion())
            .isCloseTo(expectedTau, within(expectedTau * 1e-6));

        // Expected: Angle θ = T*L/(G*J) with G=79.4e9
        double expectedTheta = 200.0 * 0.5 / (79.4e9 * J);
        assertThat(resp.getDisplacementTorsion())
            .isCloseTo(expectedTheta, within(expectedTheta * 1e-6 + 1e-15));

        // Verify formula string
        assertThat(resp.getFormulaUsed()).contains("tau = T*r/J");
    }

    @Test
    void testTorsionLoadWithCustomShearModulus() {
        // Setup: Same donut but with custom G
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.DONUT);
        req.setLoadType(LoadType.TORSION);
        req.setLength(0.5);
        req.setOuterDiameter(0.2);
        req.setInnerDiameter(0.1);
        req.setTorque(200);
        req.setShearModulus(85e9); // Custom G (higher than default 79.4e9)

        CalculationResponse resp = service.calculate(req);

        double J = Math.PI * (Math.pow(0.2, 4) - Math.pow(0.1, 4)) / 32.0;

        // Expected: Angle with custom G
        double expectedTheta = 200.0 * 0.5 / (85e9 * J);
        assertThat(resp.getDisplacementTorsion())
            .isCloseTo(expectedTheta, within(expectedTheta * 1e-6 + 1e-15));

        // Verify it differs from default steel G
        double defaultTheta = 200.0 * 0.5 / (79.4e9 * J);
        assertThat(resp.getDisplacementTorsion())
            .isNotCloseTo(defaultTheta, within(defaultTheta * 1e-3));
    }

    @Test
    void testShearLoadIBeam() {
        // Setup: I-beam, h=0.3, b=0.15, tf=0.02, tw=0.015, shear=5000, length=2.0
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.I_SHAPED);
        req.setLoadType(LoadType.SHEAR);
        req.setLength(2.0);
        req.setHeight(0.3);
        req.setWidth(0.15);
        req.setFlangeThickness(0.02);
        req.setWebThickness(0.015);
        req.setShearForce(5000);

        CalculationResponse resp = service.calculate(req);

        // Verify shear stress and displacement are non-null and numeric
        assertThat(resp.getStressShear()).isNotNull().isGreaterThan(0);
        assertThat(resp.getDisplacementShear()).isNotNull().isGreaterThan(0);

        // Verify formula string
        assertThat(resp.getFormulaUsed()).contains("tau = V*Q/(I*t)");

        // Verify section properties include Q and t
        assertThat(resp.getSectionProperties().getFirstMomentQ()).isGreaterThan(0);
        assertThat(resp.getSectionProperties().getThicknessT()).isCloseTo(0.015, within(1e-9));
    }

    @Test
    void testNegativeAxialForceHandling() {
        // Setup: Negative force should produce negative stress (compression)
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.CYLINDER);
        req.setLoadType(LoadType.AXIAL);
        req.setLength(1.0);
        req.setOuterDiameter(0.1);
        req.setAxialForce(-1000); // Negative force

        CalculationResponse resp = service.calculate(req);

        // Expected: Negative stress
        double area = Math.PI * 0.01 / 4.0;
        double expectedStress = -1000.0 / area;
        assertThat(resp.getStressTractionCompression())
            .isCloseTo(expectedStress, within(Math.abs(expectedStress) * 1e-6));
    }

    @Test
    void testZeroForceReturnsZeroStress() {
        // Setup: Zero force should produce zero stress and displacement
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.SQUARE);
        req.setLoadType(LoadType.AXIAL);
        req.setLength(1.0);
        req.setSideLength(0.2);
        req.setAxialForce(0); // Zero force

        CalculationResponse resp = service.calculate(req);

        // Expected: Zero stress and displacement
        assertThat(resp.getStressTractionCompression()).isCloseTo(0, within(1e-12));
        assertThat(resp.getDisplacementTractionCompression()).isCloseTo(0, within(1e-12));
    }

    @Test
    void testSectionPropertiesAllFieldsPopulated() {
        // Verify that all 6 fields of SectionProperties are included in response
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.HOLLOW_SQUARE);
        req.setLoadType(LoadType.TORSION);
        req.setLength(1.0);
        req.setOuterSize(0.3);
        req.setWallThickness(0.02);
        req.setTorque(100);

        CalculationResponse resp = service.calculate(req);

        // All 6 fields should be present and non-zero (except possibly some edge cases)
        assertThat(resp.getSectionProperties()).isNotNull();
        assertThat(resp.getSectionProperties().getArea()).isGreaterThan(0);
        assertThat(resp.getSectionProperties().getMomentOfInertia()).isGreaterThan(0);
        assertThat(resp.getSectionProperties().getPolarMoment()).isGreaterThan(0);
        assertThat(resp.getSectionProperties().getMaxRadius()).isGreaterThan(0);
        assertThat(resp.getSectionProperties().getFirstMomentQ()).isGreaterThan(0);
        assertThat(resp.getSectionProperties().getThicknessT()).isGreaterThan(0);
    }

    @Test
    void testTBeamPropertiesAfterBugFix() {
        // Verify T-beam calculations with corrected formula
        CalculationRequest req = new CalculationRequest();
        req.setCrossSection(CrossSection.T_SHAPED);
        req.setLoadType(LoadType.SHEAR);
        req.setLength(2.0);
        req.setHeight(0.3);
        req.setWidth(0.15);
        req.setFlangeThickness(0.02);
        req.setWebThickness(0.015);
        req.setShearForce(1000);

        CalculationResponse resp = service.calculate(req);

        // With corrected If and Iw formulas, moment of inertia should be reasonable
        assertThat(resp.getSectionProperties().getMomentOfInertia()).isGreaterThan(0);

        // Shear stress should be calculable without NaN
        assertThat(resp.getStressShear()).isFinite();
        assertThat(resp.getDisplacementShear()).isFinite();
    }
}
