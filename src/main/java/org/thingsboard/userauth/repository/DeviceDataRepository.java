package org.thingsboard.userauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thingsboard.userauth.model.DeviceData;

@Repository
public interface DeviceDataRepository extends JpaRepository<DeviceData, Long> {
    // JpaRepository provides basic CRUD operations
    // No need to write any method for saving or finding all
}
