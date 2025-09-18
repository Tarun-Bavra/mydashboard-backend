package org.thingsboard.userauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thingsboard.userauth.dto.DeviceDataDTO;
import org.thingsboard.userauth.model.Device;
import org.thingsboard.userauth.model.DeviceData;
import org.thingsboard.userauth.repository.DeviceDataRepository;
import org.thingsboard.userauth.repository.DeviceRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceDataService {

    private final DeviceDataRepository deviceDataRepository;
    private final DeviceRepository deviceRepository;

    // Fetch all device data from PostgreSQL
    public List<DeviceData> getAllDeviceData() {
        return deviceDataRepository.findAll();
    }

    // Fetch latest N records
    public List<DeviceData> getLatestDeviceData(int limit) {
        List<DeviceData> allData = deviceDataRepository.findAll();
        int size = allData.size();
        if (size <= limit) {
            return allData;
        }
        return allData.subList(size - limit, size);
    }

    // Save new telemetry
    public DeviceData saveDeviceData(DeviceDataDTO dto) {
        // Fetch the device entity first
        Device device = deviceRepository.findById(dto.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Device not found with id: " + dto.getDeviceId()));

        DeviceData deviceData = DeviceData.builder()
                .temperature(dto.getTemperature())
                .humidity(dto.getHumidity())
                .timestamp(LocalDateTime.now())
                .device(device) // link to device
                .build();

        return deviceDataRepository.save(deviceData);
    }

}
