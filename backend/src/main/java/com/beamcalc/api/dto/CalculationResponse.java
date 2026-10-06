package com.beamcalc.api.dto;

public class CalculationResponse {

    private Double stressTractionCompression;
    private Double stressTorsion;
    private Double stressShear;
    private Double displacementTractionCompression;
    private Double displacementTorsion;
    private Double displacementShear;
    private String formulaUsed;
    private SectionProperties sectionProperties;

    public CalculationResponse() {}

    public static class SectionProperties {
        private double area;
        private double momentOfInertia;
        private double polarMoment;
        private double maxRadius;
        public SectionProperties(double area, double momentOfInertia, double polarMoment, double maxRadius) {
            this.area = area; this.momentOfInertia = momentOfInertia; this.polarMoment = polarMoment; this.maxRadius = maxRadius;
        }
        public double getArea() { return area; }
        public double getMomentOfInertia() { return momentOfInertia; }
        public double getPolarMoment() { return polarMoment; }
        public double getMaxRadius() { return maxRadius; }
    }

    public Double getStressTractionCompression() { return stressTractionCompression; }
    public void setStressTractionCompression(Double v) { this.stressTractionCompression = v; }
    public Double getStressTorsion() { return stressTorsion; }
    public void setStressTorsion(Double v) { this.stressTorsion = v; }
    public Double getStressShear() { return stressShear; }
    public void setStressShear(Double v) { this.stressShear = v; }
    public Double getDisplacementTractionCompression() { return displacementTractionCompression; }
    public void setDisplacementTractionCompression(Double v) { this.displacementTractionCompression = v; }
    public Double getDisplacementTorsion() { return displacementTorsion; }
    public void setDisplacementTorsion(Double v) { this.displacementTorsion = v; }
    public Double getDisplacementShear() { return displacementShear; }
    public void setDisplacementShear(Double v) { this.displacementShear = v; }
    public String getFormulaUsed() { return formulaUsed; }
    public void setFormulaUsed(String formulaUsed) { this.formulaUsed = formulaUsed; }
    public SectionProperties getSectionProperties() { return sectionProperties; }
    public void setSectionProperties(SectionProperties p) { this.sectionProperties = p; }
}
