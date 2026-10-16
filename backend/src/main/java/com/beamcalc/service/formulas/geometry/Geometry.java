package com.beamcalc.service.formulas.geometry;

import com.beamcalc.service.formulas.SectionProperties;

/**
 * Interface representing a geometric cross-section.
 * Provides a seam for polymorphic geometry calculations.
 */
public interface Geometry {
    /**
     * Computes the geometric properties (area, moment of inertia, etc.) for this cross-section.
     * @return SectionProperties containing the calculated geometric values
     */
    SectionProperties computeProperties();
}