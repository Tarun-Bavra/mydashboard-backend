package org.thingsboard.userauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.userauth.dto.TelemetryDto;
import org.thingsboard.userauth.model.Telemetry;
import org.thingsboard.userauth.service.TelemetryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/telemetry")
@RequiredArgsConstructor
public class TelemetryController {

    private final TelemetryService telemetryService;

    // ✅ GET last 20 readings
    @GetMapping("/recent")
    public ResponseEntity<List<TelemetryDto>> getRecentReadings() {
        List<Telemetry> readings = telemetryService.getRecentReadings();

        // Convert entity -> DTO
        List<TelemetryDto> dtos = readings.stream()
                .map(t -> new TelemetryDto(t.getTemperature(), t.getHumidity(), t.getTimestamp()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // ✅ POST a new telemetry reading
    @PostMapping("/add")
    public ResponseEntity<TelemetryDto> addReading(@RequestBody TelemetryDto dto) {
        Telemetry saved = telemetryService.saveReading(dto.getTemperature(), dto.getHumidity());
        TelemetryDto response = new TelemetryDto(saved.getTemperature(), saved.getHumidity(), saved.getTimestamp());
        return ResponseEntity.ok(response);
    }
}
