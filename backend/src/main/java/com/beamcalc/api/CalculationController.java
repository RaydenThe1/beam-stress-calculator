package com.beamcalc.api;

import com.beamcalc.api.dto.CalculationRequest;
import com.beamcalc.api.dto.CalculationResponse;
import com.beamcalc.service.CalculatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CalculationController {

    private final CalculatorService service;
    public CalculationController(CalculatorService service) { this.service = service; }

    @PostMapping("/calculate")
    public ResponseEntity<CalculationResponse> calculate(@Valid @RequestBody CalculationRequest r) {
        return ResponseEntity.ok(service.calculate(r));
    }
}
