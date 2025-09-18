package org.thingsboard.userauth.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "devices", uniqueConstraints = @UniqueConstraint(columnNames = "device_identifier"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;                // human friendly name, e.g. "Room-101 Sensor"

    @Column(name = "device_identifier", nullable = false, unique = true)
    private String deviceIdentifier;    // unique id used by simulator, e.g. "dev-001"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;                 // owner user (optional), FK to users table

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceData> telemetryData;

}
