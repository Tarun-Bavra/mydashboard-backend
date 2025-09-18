package org.thingsboard.userauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thingsboard.userauth.model.Alarm;
import org.thingsboard.userauth.model.AlarmSeverity;
import org.thingsboard.userauth.model.Device;
import org.thingsboard.userauth.repository.AlarmRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    // Create and save a new alarm
    public Alarm createAlarm(Device device, String message, AlarmSeverity severity, String status) {
        Alarm alarm = Alarm.builder()
                .device(device) // ✅ correct entity type
                .type(message)  // store type of alarm (can be same as message or different)
                .message(message)
                .severity(severity)
                .status(status != null ? status : "ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        return alarmRepository.save(alarm);
    }

    // Get all alarms with device info
    public List<Alarm> getAllAlarms() {
        return alarmRepository.findAll();
    }

    // Get alarms for a specific device
    public List<Alarm> getAlarmsByDevice(Device device) {
        return alarmRepository.findByDevice(device);
    }

    // Get active alarms for a device
    public List<Alarm> getActiveAlarms(Device device) {
        return alarmRepository.findByDeviceAndStatus(device, "ACTIVE");
    }

    // Get alarms by severity
    public List<Alarm> getAlarmsBySeverity(AlarmSeverity severity) {
        return alarmRepository.findBySeverity(severity);
    }

    // Update alarm status (ACKNOWLEDGED / CLEARED)
    public Alarm updateStatus(Long id, String status) {
        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alarm not found with id: " + id));

        alarm.setStatus(status);

        if ("ACKNOWLEDGED".equalsIgnoreCase(status)) {
            alarm.setAcknowledgedAt(LocalDateTime.now());
        } else if ("CLEARED".equalsIgnoreCase(status)) {
            alarm.setClearedAt(LocalDateTime.now());
        }

        return alarmRepository.save(alarm);
    }
}
