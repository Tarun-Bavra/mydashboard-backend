package org.thingsboard.userauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.thingsboard.userauth.model.Device;
import org.thingsboard.userauth.repository.DeviceRepository;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceRepository deviceRepository;

    // Get device by ID
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));
        return ResponseEntity.ok(device);
    }

    // Create a new device
    @PostMapping("/create")
    public ResponseEntity<Device> createDevice(@RequestBody Device deviceRequest) {
        // Check if device identifier already exists
        deviceRepository.findByDeviceIdentifier(deviceRequest.getDeviceIdentifier()).ifPresent(d -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Device identifier already exists");
        });

        Device device = Device.builder()
                .name(deviceRequest.getName())
                .deviceIdentifier(deviceRequest.getDeviceIdentifier())
                .createdAt(LocalDateTime.now())
                .build();

        Device saved = deviceRepository.save(device);
        return ResponseEntity.ok(saved);
    }
}
