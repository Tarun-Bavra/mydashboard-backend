package org.thingsboard.userauth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "telemetry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Temperature in °C
    @Column(nullable = false)
    private Double temperature;

    // Humidity in %
    @Column(nullable = false)
    private Double humidity;

    // Timestamp when reading was recorded
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
