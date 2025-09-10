package org.thingsboard.userauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.userauth.model.DeviceData;
import org.thingsboard.userauth.service.DeviceDataService;

import java.util.List;

@RestController
@RequestMapping("/api/device-data")
@RequiredArgsConstructor
public class DeviceDataController {

    private final DeviceDataService deviceDataService;

    // Get all data
    @GetMapping
    public ResponseEntity<List<DeviceData>> getAllDeviceData() {
        List<DeviceData> data = deviceDataService.getAllDeviceData();
        return ResponseEntity.ok(data);
    }

    // Get latest N records
    @GetMapping("/latest/{limit}")
    public ResponseEntity<List<DeviceData>> getLatestDeviceData(@PathVariable int limit) {
        List<DeviceData> data = deviceDataService.getLatestDeviceData(limit);
        return ResponseEntity.ok(data);
    }
}
