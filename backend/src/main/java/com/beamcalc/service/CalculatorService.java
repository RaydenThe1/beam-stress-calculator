package com.beamcalc.service;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.api.dto.CalculationResponse;
import com.beamcalc.model.CrossSection;
import com.beamcalc.model.LoadType;
import com.beamcalc.service.formulas.SectionProperties;
import com.beamcalc.service.formulas.StressFormulas;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    private static final double E_STEEL = 200e9;
    private static final double G_STEEL = 79.4e9;

    public CalculationResponse calculate(CalculationRequest r) {
        CrossSection s = r.getCrossSection();
        LoadType lt = r.getLoadType();
        double E = r.getYoungsModulus() != null ? r.getYoungsModulus() : E_STEEL;
        double G = r.getShearModulus() != null ? r.getShearModulus() : G_STEEL;

        SectionProperties sp = StressFormulas.compute(s, r);
        CalculationResponse res = new CalculationResponse();
        res.setSectionProperties(res.new SectionProperties(sp.area, sp.momentOfInertia, sp.polarMoment, sp.maxRadius, sp.firstMomentQ, sp.thicknessT));

        switch (lt) {
            case AXIAL -> {
                double stress = r.getAxialForce() / sp.area;
                double disp = r.getAxialForce() * r.getLength() / (sp.area * E);
                res.setStressTractionCompression(stress);
                res.setDisplacementTractionCompression(disp);
                res.setFormulaUsed("sigma = F/A, delta = F*L/(A*E)");
            }
            case TORSION -> {
                double tau = r.getTorque() * sp.maxRadius / sp.polarMoment;
                double theta = r.getTorque() * r.getLength() / (G * sp.polarMoment);
                res.setStressTorsion(tau);
                res.setDisplacementTorsion(theta);
                res.setFormulaUsed("tau = T*r/J, theta = T*L/(G*J)");
            }
            case SHEAR -> {
                double tau = r.getShearForce() * sp.firstMomentQ / (sp.momentOfInertia * sp.thicknessT);
                double delta = r.getShearForce() * r.getLength() * r.getLength() * r.getLength() / (48.0 * E * sp.momentOfInertia);
                res.setStressShear(tau);
                res.setDisplacementShear(delta);
                res.setFormulaUsed("tau = V*Q/(I*t), delta = V*L^3/(48*E*I)");
            }
        }
        return res;
    }
}
