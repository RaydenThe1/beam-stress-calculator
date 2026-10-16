package com.beamcalc.service.loaders;

/**
 * Result of a load calculation, containing stress, displacement, and the formula used.
 */
public class LoadResult {
    private final Double stress;
    private final Double displacement;
    private final String formulaUsed;
    private final String stressLabel;
    private final String displacementLabel;

    public LoadResult(Double stress, Double displacement, String formulaUsed,
                     String stressLabel, String displacementLabel) {
        this.stress = stress;
        this.displacement = displacement;
        this.formulaUsed = formulaUsed;
        this.stressLabel = stressLabel;
        this.displacementLabel = displacementLabel;
    }

    public Double getStress() {
        return stress;
    }

    public Double getDisplacement() {
        return displacement;
    }

    public String getFormulaUsed() {
        return formulaUsed;
    }

    public String getStressLabel() {
        return stressLabel;
    }

    public String getDisplacementLabel() {
        return displacementLabel;
    }
}
