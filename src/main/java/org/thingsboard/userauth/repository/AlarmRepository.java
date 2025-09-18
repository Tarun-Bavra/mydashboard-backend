package org.thingsboard.userauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thingsboard.userauth.model.Alarm;
import org.thingsboard.userauth.model.AlarmSeverity;
import org.thingsboard.userauth.model.Device;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    /**
     * Fetch all alarms with associated device (eager fetch)
     */
    @Query("SELECT a FROM Alarm a JOIN FETCH a.device")
    List<Alarm> findAllWithDevice();

    /**
     * Fetch alarms for a specific device
     */
    @Query("SELECT a FROM Alarm a JOIN FETCH a.device WHERE a.device = :device")
    List<Alarm> findByDevice(@Param("device") Device device);

    /**
     * Fetch active alarms for a specific device
     */
    @Query("SELECT a FROM Alarm a JOIN FETCH a.device WHERE a.device = :device AND a.status = :status")
    List<Alarm> findByDeviceAndStatus(@Param("device") Device device, @Param("status") String status);

    /**
     * Fetch alarms by severity
     */
    @Query("SELECT a FROM Alarm a JOIN FETCH a.device WHERE a.severity = :severity")
    List<Alarm> findBySeverity(@Param("severity") AlarmSeverity severity);
}
