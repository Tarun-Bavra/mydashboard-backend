package org.thingsboard.userauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thingsboard.userauth.model.DeviceData;

@Repository
public interface DeviceDataRepository extends JpaRepository<DeviceData, Long> {
    // Only DeviceData-related queries go here
    // (no deviceIdentifier since that's in Device, not DeviceData)
}
