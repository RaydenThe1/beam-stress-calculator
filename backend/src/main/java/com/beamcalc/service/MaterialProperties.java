package com.beamcalc.service;

/**
 * Represents the material properties of a beam (Young's modulus and shear modulus).
 * Centralizes material constants to avoid duplication across the codebase.
 */
public class MaterialProperties {
    public final double youngsModulus;
    public final double shearModulus;

    public MaterialProperties(double youngsModulus, double shearModulus) {
        this.youngsModulus = youngsModulus;
        this.shearModulus = shearModulus;
    }

    /**
     * Steel material properties: E = 200 GPa, G = 79.4 GPa
     */
    public static final MaterialProperties STEEL = new MaterialProperties(200e9, 79.4e9);

    /**
     * Default material (steel)
     */
    public static MaterialProperties getDefault() {
        return STEEL;
    }
}
