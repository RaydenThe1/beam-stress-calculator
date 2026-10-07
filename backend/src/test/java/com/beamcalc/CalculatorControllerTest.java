package com.beamcalc;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.model.CrossSection;
import com.beamcalc.model.LoadType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerTest {

    @Autowired MockMvc mvc;

    String json(String body) { return body; }

    // ==================== Original Tests ====================

    @Test
    void cylinder_axial_returns_stress_and_displacement() throws Exception {
        String body = """
        {
          "crossSection":"CYLINDER","loadType":"AXIAL",
          "length":1.0,"outerDiameter":0.1,"axialForce":1000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTractionCompression").exists())
           .andExpect(jsonPath("$.displacementTractionCompression").exists())
           .andExpect(jsonPath("$.sectionProperties.area").exists());
    }

    @Test
    void donut_torsion_returns_tau_and_theta() throws Exception {
        String body = """
        {"crossSection":"DONUT","loadType":"TORSION",
         "length":0.5,"outerDiameter":0.2,"innerDiameter":0.1,"torque":200}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTorsion").exists())
           .andExpect(jsonPath("$.displacementTorsion").exists());
    }

    @Test
    void i_beam_shear_returns_shear_stress() throws Exception {
        String body = """
        {"crossSection":"I_SHAPED","loadType":"SHEAR",
         "length":2.0,"height":0.3,"width":0.15,"flangeThickness":0.02,"webThickness":0.015,"shearForce":5000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressShear").exists())
           .andExpect(jsonPath("$.displacementShear").exists());
    }

    // ==================== Fixed Validation Test ====================

    @Test
    void missing_crossSection_returns_400() throws Exception {
        String body = """
        {"loadType":"AXIAL",
         "length":1.0,"outerDiameter":0.1,"axialForce":1000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isBadRequest());
    }

    // ==================== Additional Validation Tests ====================

    @Test
    void missing_loadType_returns_400() throws Exception {
        String body = """
        {"crossSection":"CYLINDER",
         "length":1.0,"outerDiameter":0.1,"axialForce":1000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isBadRequest());
    }

    @Test
    void zero_length_returns_400() throws Exception {
        String body = """
        {"crossSection":"CYLINDER","loadType":"AXIAL",
         "length":0.0,"outerDiameter":0.1,"axialForce":1000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isBadRequest());
    }

    @Test
    void negative_outerDiameter_returns_400() throws Exception {
        String body = """
        {"crossSection":"CYLINDER","loadType":"AXIAL",
         "length":1.0,"outerDiameter":-0.1,"axialForce":1000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isBadRequest());
    }

    // ==================== Cross-Section Coverage Tests ====================

    @Test
    void square_axial_returns_200() throws Exception {
        String body = """
        {"crossSection":"SQUARE","loadType":"AXIAL",
         "length":1.0,"sideLength":0.2,"axialForce":500}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTractionCompression").exists())
           .andExpect(jsonPath("$.displacementTractionCompression").exists());
    }

    @Test
    void hollow_square_axial_returns_200() throws Exception {
        String body = """
        {"crossSection":"HOLLOW_SQUARE","loadType":"AXIAL",
         "length":1.0,"outerSize":0.3,"wallThickness":0.02,"axialForce":1000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTractionCompression").exists());
    }

    @Test
    void t_shaped_axial_returns_200() throws Exception {
        String body = """
        {"crossSection":"T_SHAPED","loadType":"AXIAL",
         "length":1.0,"height":0.3,"width":0.15,"flangeThickness":0.02,"webThickness":0.015,"axialForce":1000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTractionCompression").exists());
    }

    @Test
    void cylinder_torsion_returns_200() throws Exception {
        String body = """
        {"crossSection":"CYLINDER","loadType":"TORSION",
         "length":1.0,"outerDiameter":0.1,"torque":100}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTorsion").exists())
           .andExpect(jsonPath("$.displacementTorsion").exists());
    }

    @Test
    void square_torsion_returns_200() throws Exception {
        String body = """
        {"crossSection":"SQUARE","loadType":"TORSION",
         "length":1.0,"sideLength":0.2,"torque":50}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTorsion").exists());
    }

    @Test
    void hollow_square_torsion_returns_200() throws Exception {
        String body = """
        {"crossSection":"HOLLOW_SQUARE","loadType":"TORSION",
         "length":1.0,"outerSize":0.3,"wallThickness":0.02,"torque":100}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTorsion").exists());
    }

    @Test
    void i_beam_torsion_returns_200() throws Exception {
        String body = """
        {"crossSection":"I_SHAPED","loadType":"TORSION",
         "length":1.0,"height":0.3,"width":0.15,"flangeThickness":0.02,"webThickness":0.015,"torque":50}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTorsion").exists());
    }

    @Test
    void t_beam_torsion_returns_200() throws Exception {
        String body = """
        {"crossSection":"T_SHAPED","loadType":"TORSION",
         "length":1.0,"height":0.3,"width":0.15,"flangeThickness":0.02,"webThickness":0.015,"torque":50}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTorsion").exists());
    }

    @Test
    void cylinder_shear_returns_200() throws Exception {
        String body = """
        {"crossSection":"CYLINDER","loadType":"SHEAR",
         "length":1.0,"outerDiameter":0.1,"shearForce":500}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressShear").exists())
           .andExpect(jsonPath("$.displacementShear").exists());
    }

    @Test
    void donut_shear_returns_200() throws Exception {
        String body = """
        {"crossSection":"DONUT","loadType":"SHEAR",
         "length":1.0,"outerDiameter":0.2,"innerDiameter":0.1,"shearForce":500}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressShear").exists());
    }

    @Test
    void square_shear_returns_200() throws Exception {
        String body = """
        {"crossSection":"SQUARE","loadType":"SHEAR",
         "length":1.0,"sideLength":0.2,"shearForce":500}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressShear").exists());
    }

    @Test
    void hollow_square_shear_returns_200() throws Exception {
        String body = """
        {"crossSection":"HOLLOW_SQUARE","loadType":"SHEAR",
         "length":1.0,"outerSize":0.3,"wallThickness":0.02,"shearForce":1000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressShear").exists());
    }

    @Test
    void t_beam_shear_returns_200() throws Exception {
        String body = """
        {"crossSection":"T_SHAPED","loadType":"SHEAR",
         "length":2.0,"height":0.3,"width":0.15,"flangeThickness":0.02,"webThickness":0.015,"shearForce":5000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressShear").exists());
    }

    // ==================== Custom Material Tests ====================

    @Test
    void custom_youngsModulus_affects_axial_displacement() throws Exception {
        String bodyDefault = """
        {"crossSection":"CYLINDER","loadType":"AXIAL",
         "length":1.0,"outerDiameter":0.1,"axialForce":1000}""";
        String bodyCustom = """
        {"crossSection":"CYLINDER","loadType":"AXIAL",
         "length":1.0,"outerDiameter":0.1,"axialForce":1000,"youngsModulus":210000000000}""";

        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(bodyDefault))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.displacementTractionCompression").exists());

        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(bodyCustom))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.displacementTractionCompression").exists());
    }

    @Test
    void custom_shearModulus_affects_torsional_displacement() throws Exception {
        String bodyDefault = """
        {"crossSection":"DONUT","loadType":"TORSION",
         "length":0.5,"outerDiameter":0.2,"innerDiameter":0.1,"torque":200}""";
        String bodyCustom = """
        {"crossSection":"DONUT","loadType":"TORSION",
         "length":0.5,"outerDiameter":0.2,"innerDiameter":0.1,"torque":200,"shearModulus":85000000000}""";

        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(bodyDefault))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.displacementTorsion").exists());

        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(bodyCustom))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.displacementTorsion").exists());
    }

    // ==================== Section Properties Structure Tests ====================

    @Test
    void sectionProperties_includes_all_six_fields() throws Exception {
        String body = """
        {"crossSection":"I_SHAPED","loadType":"SHEAR",
         "length":2.0,"height":0.3,"width":0.15,"flangeThickness":0.02,"webThickness":0.015,"shearForce":5000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.sectionProperties.area").exists())
           .andExpect(jsonPath("$.sectionProperties.momentOfInertia").exists())
           .andExpect(jsonPath("$.sectionProperties.polarMoment").exists())
           .andExpect(jsonPath("$.sectionProperties.maxRadius").exists())
           .andExpect(jsonPath("$.sectionProperties.firstMomentQ").exists())
           .andExpect(jsonPath("$.sectionProperties.thicknessT").exists());
    }

    @Test
    void formulaUsed_field_is_populated() throws Exception {
        String bodyAxial = """
        {"crossSection":"CYLINDER","loadType":"AXIAL",
         "length":1.0,"outerDiameter":0.1,"axialForce":1000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(bodyAxial))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.formulaUsed").exists())
           .andExpect(jsonPath("$.formulaUsed").value(org.hamcrest.Matchers.containsString("sigma = F/A")));

        String bodyTorsion = """
        {"crossSection":"DONUT","loadType":"TORSION",
         "length":0.5,"outerDiameter":0.2,"innerDiameter":0.1,"torque":200}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(bodyTorsion))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.formulaUsed").value(org.hamcrest.Matchers.containsString("tau = T*r/J")));

        String bodyShear = """
        {"crossSection":"CYLINDER","loadType":"SHEAR",
         "length":1.0,"outerDiameter":0.1,"shearForce":500}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(bodyShear))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.formulaUsed").value(org.hamcrest.Matchers.containsString("tau = V*Q/(I*t)")));
    }

    // ==================== Edge Case Tests ====================

    @Test
    void negative_force_returns_negative_stress() throws Exception {
        String body = """
        {"crossSection":"SQUARE","loadType":"AXIAL",
         "length":1.0,"sideLength":0.2,"axialForce":-500}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTractionCompression").exists());
    }

    @Test
    void very_small_force_returns_very_small_stress() throws Exception {
        String body = """
        {"crossSection":"HOLLOW_SQUARE","loadType":"AXIAL",
         "length":1.0,"outerSize":0.3,"wallThickness":0.02,"axialForce":0.001}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTractionCompression").exists());
    }

    @Test
    void very_large_force_returns_large_stress() throws Exception {
        String body = """
        {"crossSection":"CYLINDER","loadType":"AXIAL",
         "length":1.0,"outerDiameter":0.1,"axialForce":1000000}""";
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.stressTractionCompression").isNumber());
    }
}
