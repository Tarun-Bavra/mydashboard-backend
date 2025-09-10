package org.thingsboard.userauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryDto {
    private Double temperature;
    private Double humidity;
    private LocalDateTime timestamp;
}
