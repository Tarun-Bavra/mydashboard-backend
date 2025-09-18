package org.thingsboard.userauth.dto;

import lombok.Builder;
import lombok.Data;
import org.thingsboard.userauth.model.Alarm;
import org.thingsboard.userauth.model.AlarmSeverity;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor   // generates full constructor
@NoArgsConstructor    // ensures JSON deserialization works
public class AlarmRequest {
    private Long id;
    private Long deviceId;
    private String message;
    private AlarmSeverity severity;
    private String status;
    private String createdAt;
    private String acknowledgedAt;
    private String clearedAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Map Alarm entity → AlarmRequest
    public AlarmRequest(Alarm alarm) {
        this.id = alarm.getId();
        this.deviceId = alarm.getDevice() != null ? alarm.getDevice().getId() : null;
        this.message = alarm.getType();
        this.severity = alarm.getSeverity();
        this.status = alarm.getStatus();
        this.createdAt = alarm.getCreatedAt() != null ? alarm.getCreatedAt().format(FORMATTER) : null;
        this.acknowledgedAt = alarm.getAcknowledgedAt() != null ? alarm.getAcknowledgedAt().format(FORMATTER) : null;
        this.clearedAt = alarm.getClearedAt() != null ? alarm.getClearedAt().format(FORMATTER) : null;
    }
}

