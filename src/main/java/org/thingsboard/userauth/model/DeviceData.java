package org.thingsboard.userauth.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "device_data")
@Getter
public class DeviceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double humidity;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Default constructor required by JPA
    public DeviceData() {}

    // Constructor to create object easily
    public DeviceData(Double temperature, Double humidity, LocalDateTime timestamp) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.timestamp = timestamp;
    }
}
