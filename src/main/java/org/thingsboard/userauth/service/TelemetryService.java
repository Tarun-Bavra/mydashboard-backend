package org.thingsboard.userauth.service;

import org.springframework.stereotype.Service;
import org.thingsboard.userauth.model.Telemetry;
import org.thingsboard.userauth.repository.TelemetryRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelemetryService {

    private final TelemetryRepository telemetryRepository;

    // Save a telemetry reading
    public Telemetry saveReading(Double temperature, Double humidity) {
        Telemetry t = new Telemetry();
        t.setTemperature(temperature);
        t.setHumidity(humidity);
        t.setTimestamp(LocalDateTime.now());
        return telemetryRepository.save(t);
    }

    // Fetch last N readings for dashboard
    public List<Telemetry> getRecentReadings() {
        return telemetryRepository.findTop20ByOrderByTimestampDesc();
    }
}
