package com.beamcalc.service.formulas;

import com.beamcalc.model.CrossSection;

public class StressFormulas {

    private static final double E_STEEL = 200e9;
    private static final double G_STEEL = 79.4e9;

    public static SectionProperties compute(CrossSection s, CalculationRequest r) {
        return switch (s) {
            case CYLINDER     -> cylinder(r.outerDiameter);
            case DONUT        -> donut(r.outerDiameter, r.innerDiameter);
            case SQUARE       -> square(r.sideLength);
            case HOLLOW_SQUARE-> hollowSquare(r.outerSize, r.wallThickness);
            case I_SHAPED     -> iBeam(r.height, r.width, r.flangeThickness, r.webThickness);
            case T_SHAPED     -> tBeam(r.height, r.width, r.flangeThickness, r.webThickness);
        };
    }

    // --- Cylinder (solid) ---
    static SectionProperties cylinder(double d) {
        double a = Math.PI * d * d / 4.0;
        double I = Math.PI * d * d * d * d / 64.0;
        double J = Math.PI * d * d * d * d / 32.0;
        double r = d / 2.0;
        return new SectionProperties(a, I, J, r, 4.0 * a / (3.0 * Math.PI * r), d); // approximate Q,t for shear
    }

    // --- Hollow cylinder (donut) ---
    static SectionProperties donut(double D, double d) {
        double a = Math.PI * (D * D - d * d) / 4.0;
        double I = Math.PI * (D * D * D * D - d * d * d * d) / 64.0;
        double J = Math.PI * (D * D * D * D - d * d * d * d) / 32.0;
        double r = D / 2.0;
        return new SectionProperties(a, I, J, r, 4.0 * a / (3.0 * Math.PI * r), D - d);
    }

    // --- Solid square ---
    static SectionProperties square(double a) {
        double A = a * a;
        double I = a * a * a * a / 12.0;
        double J = 0.142 * a * a * a * a; // St. Venant torsional constant
        return new SectionProperties(A, I, J, a / 2.0, A / (2.0 * a), a);
    }

    // --- Hollow square ---
    static SectionProperties hollowSquare(double a, double t) {
        double inner = a - 2.0 * t;
        double A = a * a - inner * inner;
        double I = (a * a * a * a - inner * inner * inner * inner) / 12.0;
        double J = 0.142 * (a * a * a * a - inner * inner * inner * inner); // approx
        return new SectionProperties(A, I, J, a / 2.0, A / (2.0 * a), t);
    }

    // --- I-beam (simplified: flange+web decomposition) ---
    static SectionProperties iBeam(double h, double b, double tf, double tw) {
        double Af = b * tf;
        double Aw = (h - 2.0 * tf) * tw;
        double A = 2.0 * Af + Aw;
        double yc = h / 2.0; // centroid on axis of symmetry
        double I = (b * h * h * h / 12.0) - ((b - tw) * (h - 2.0 * tf) * (h - 2.0 * tf) * (h - 2.0 * tf) / 12.0);
        double J = 0.142 * h * h * h * h; // approx for thin-walled I
        double Q = Af * (h / 2.0 - tf / 2.0); // first moment at web-flange junction
        return new SectionProperties(A, I, J, h / 2.0, Q, tw);
    }

    // --- T-beam ---
    static SectionProperties tBeam(double h, double b, double tf, double tw) {
        double flangeA = b * tf;
        double webA = (h - tf) * tw;
        double A = flangeA + webA;
        double yc = (flangeA * (h - tf / 2.0) + webA * (h - tf) / 2.0) / A;
        double If = b * b * b * tf / 12.0;
        double Iw = tw * tw * (h - tf) / 12.0;
        double I = If + flangeA * Math.pow((h - tf / 2.0) - yc, 2) + Iw + webA * Math.pow((h - tf) / 2.0 - yc, 2);
        double J = 0.142 * h * h * h * h; // approx
        double Q = flangeA * ((h - tf / 2.0) - yc);
        return new SectionProperties(A, I, J, h - yc, Q, tw);
    }
}
