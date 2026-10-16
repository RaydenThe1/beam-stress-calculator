package com.beamcalc.service;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.api.dto.CalculationResponse;
import com.beamcalc.service.formulas.SectionProperties;
import com.beamcalc.service.formulas.geometry.GeometryFactory;
import com.beamcalc.service.loaders.LoadCalculator;
import com.beamcalc.service.loaders.LoadCalculatorFactory;
import com.beamcalc.service.loaders.LoadResult;
import org.springframework.stereotype.Service;

/**
 * CalculatorService orchestrates the beam stress calculation pipeline.
 *
 * Pipeline: Geometry → Load Calculator → Response
 *
 * Uses injected factories for geometry and load type selection,
 * enabling polymorphic seams for extensibility.
 */
@Service
public class CalculatorService {

    private final GeometryFactory geometryFactory;
    private final LoadCalculatorFactory loadCalculatorFactory;

    public CalculatorService(GeometryFactory geometryFactory, LoadCalculatorFactory loadCalculatorFactory) {
        this.geometryFactory = geometryFactory;
        this.loadCalculatorFactory = loadCalculatorFactory;
    }

    public CalculationResponse calculate(CalculationRequest r) {
        // Resolve material properties (default to steel if not specified)
        MaterialProperties material = (r.getYoungsModulus() != null || r.getShearModulus() != null)
            ? new MaterialProperties(
                r.getYoungsModulus() != null ? r.getYoungsModulus() : MaterialProperties.STEEL.youngsModulus,
                r.getShearModulus() != null ? r.getShearModulus() : MaterialProperties.STEEL.shearModulus
              )
            : MaterialProperties.getDefault();

        // Step 1: Compute geometry properties
        SectionProperties sp = geometryFactory.create(r.getCrossSection(), r).computeProperties();

        // Step 2: Compute load result
        LoadCalculator loadCalculator = loadCalculatorFactory.create(r.getLoadType());
        LoadResult loadResult = loadCalculator.calculate(sp, r, material);

        // Step 3: Build response
        CalculationResponse res = new CalculationResponse();
        res.setSectionProperties(res.new SectionProperties(sp.area, sp.momentOfInertia, sp.polarMoment, sp.maxRadius));
        res.setFormulaUsed(loadResult.getFormulaUsed());

        // Apply load result to appropriate response fields based on stress/displacement labels
        applyLoadResult(res, loadResult);

        return res;
    }

    private void applyLoadResult(CalculationResponse res, LoadResult loadResult) {
        String stressField = loadResult.getStressLabel();
        String displacementField = loadResult.getDisplacementLabel();

        switch (stressField) {
            case "stressTractionCompression" -> res.setStressTractionCompression(loadResult.getStress());
            case "stressTorsion" -> res.setStressTorsion(loadResult.getStress());
            case "stressShear" -> res.setStressShear(loadResult.getStress());
        }

        switch (displacementField) {
            case "displacementTractionCompression" -> res.setDisplacementTractionCompression(loadResult.getDisplacement());
            case "displacementTorsion" -> res.setDisplacementTorsion(loadResult.getDisplacement());
            case "displacementShear" -> res.setDisplacementShear(loadResult.getDisplacement());
        }
    }
}