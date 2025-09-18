package org.thingsboard.userauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.userauth.dto.AlarmRequest;
import org.thingsboard.userauth.model.Alarm;
import org.thingsboard.userauth.model.AlarmSeverity;
import org.thingsboard.userauth.model.Device;
import org.thingsboard.userauth.repository.DeviceRepository;
import org.thingsboard.userauth.service.AlarmService;

import java.util.List;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    private final DeviceRepository deviceRepository; // Correct repository for Device

    /**
     * Create a new alarm for a device
     */
    @PostMapping("/create")
    public ResponseEntity<AlarmRequest> createAlarm(@RequestBody AlarmRequest alarmRequest) {
        Device device = deviceRepository.findById(alarmRequest.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + alarmRequest.getDeviceId()));

        Alarm alarm = alarmService.createAlarm(
                device,
                alarmRequest.getMessage(),
                alarmRequest.getSeverity(),
                alarmRequest.getStatus()
        );

        // Convert to DTO for frontend
        AlarmRequest dto = new AlarmRequest(alarm);
        return ResponseEntity.ok(dto);
    }

    /**
     * Get all alarms
     */
    @GetMapping
    public ResponseEntity<List<AlarmRequest>> getAllAlarms() {
        List<AlarmRequest> dtos = alarmService.getAllAlarms().stream()
                .map(AlarmRequest::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get all alarms for a specific device
     */
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<AlarmRequest>> getAlarmsByDevice(@PathVariable Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

        List<AlarmRequest> dtos = alarmService.getAlarmsByDevice(device).stream()
                .map(AlarmRequest::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get all active alarms for a device
     */
    @GetMapping("/device/{deviceId}/active")
    public ResponseEntity<List<AlarmRequest>> getActiveAlarms(@PathVariable Long deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

        List<AlarmRequest> dtos = alarmService.getActiveAlarms(device).stream()
                .map(AlarmRequest::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Get alarms by severity
     */
    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<AlarmRequest>> getAlarmsBySeverity(@PathVariable AlarmSeverity severity) {
        List<AlarmRequest> dtos = alarmService.getAlarmsBySeverity(severity).stream()
                .map(AlarmRequest::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Update alarm status (ACKNOWLEDGED / CLEARED)
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<AlarmRequest> updateAlarmStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Alarm alarm = alarmService.updateStatus(id, status);
        return ResponseEntity.ok(new AlarmRequest(alarm));
    }
}
