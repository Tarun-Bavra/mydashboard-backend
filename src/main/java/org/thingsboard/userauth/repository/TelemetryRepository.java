package org.thingsboard.userauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thingsboard.userauth.model.Telemetry;

import java.util.List;

@Repository
public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {

    // Fetch last N readings ordered by timestamp descending
    List<Telemetry> findTop20ByOrderByTimestampDesc();
}
