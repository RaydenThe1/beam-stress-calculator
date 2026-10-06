package com.beamcalc.api.dto;

import com.beamcalc.model.CrossSection;
import com.beamcalc.model.LoadType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CalculationRequest {

    @NotNull(message = "crossSection is required")
    private CrossSection crossSection;

    @NotNull(message = "loadType is required")
    private LoadType loadType;

    private double length;
    private double outerDiameter;
    private double innerDiameter;
    private double height;
    private double width;
    private double flangeThickness;
    private double webThickness;
    private double outerSize;
    private double wallThickness;
    private double sideLength;
    private double axialForce;
    private double torque;
    private double shearForce;
    private Double youngsModulus;
    private Double shearModulus;

    public CrossSection getCrossSection() { return crossSection; }
    public void setCrossSection(CrossSection crossSection) { this.crossSection = crossSection; }
    public LoadType getLoadType() { return loadType; }
    public void setLoadType(LoadType loadType) { this.loadType = loadType; }
    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }
    public double getOuterDiameter() { return outerDiameter; }
    public void setOuterDiameter(double outerDiameter) { this.outerDiameter = outerDiameter; }
    public double getInnerDiameter() { return innerDiameter; }
    public void setInnerDiameter(double innerDiameter) { this.innerDiameter = innerDiameter; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }
    public double getFlangeThickness() { return flangeThickness; }
    public void setFlangeThickness(double flangeThickness) { this.flangeThickness = flangeThickness; }
    public double getWebThickness() { return webThickness; }
    public void setWebThickness(double webThickness) { this.webThickness = webThickness; }
    public double getOuterSize() { return outerSize; }
    public void setOuterSize(double outerSize) { this.outerSize = outerSize; }
    public double getWallThickness() { return wallThickness; }
    public void setWallThickness(double wallThickness) { this.wallThickness = wallThickness; }
    public double getSideLength() { return sideLength; }
    public void setSideLength(double sideLength) { this.sideLength = sideLength; }
    public double getAxialForce() { return axialForce; }
    public void setAxialForce(double axialForce) { this.axialForce = axialForce; }
    public double getTorque() { return torque; }
    public void setTorque(double torque) { this.torque = torque; }
    public double getShearForce() { return shearForce; }
    public void setShearForce(double shearForce) { this.shearForce = shearForce; }
    public Double getYoungsModulus() { return youngsModulus; }
    public void setYoungsModulus(Double youngsModulus) { this.youngsModulus = youngsModulus; }
    public Double getShearModulus() { return shearModulus; }
    public void setShearModulus(Double shearModulus) { this.shearModulus = shearModulus; }
}
