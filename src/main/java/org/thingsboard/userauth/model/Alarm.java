package org.thingsboard.userauth.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alarms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private LocalDateTime acknowledgedAt;
    private LocalDateTime clearedAt;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;   // Which device triggered this alarm

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private AlarmSeverity severity;   // CRITICAL, MAJOR, MINOR, etc.

    @Column(name = "type", nullable = false)
    private String type;    // e.g., "TemperatureThresholdExceeded"

    @Column(name = "status", nullable = false)
    private String status;  // e.g., "ACTIVE", "ACKNOWLEDGED", "CLEARED"

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "ACTIVE"; // Default status when alarm is created
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
