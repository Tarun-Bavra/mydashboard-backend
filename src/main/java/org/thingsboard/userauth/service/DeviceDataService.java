package org.thingsboard.userauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thingsboard.userauth.model.DeviceData;
import org.thingsboard.userauth.repository.DeviceDataRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceDataService {

    private final DeviceDataRepository deviceDataRepository;

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
}
