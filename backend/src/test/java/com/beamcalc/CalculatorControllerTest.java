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

    @Test
    void invalid_request_returns_400() throws Exception {
        String body = """
        {"crossSection":"CYLINDER","loadType":"AXIAL",
         "length":1.0,"outerDiameter":0.1}"""; // missing axialForce
        mvc.perform(post("/api/v1/calculate").contentType(MediaType.APPLICATION_JSON).content(body))
           .andExpect(status().isBadRequest());
    }
}
