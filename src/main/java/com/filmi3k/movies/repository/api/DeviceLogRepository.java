package com.filmi3k.movies.repository.api;

import com.filmi3k.movies.domain.entities.DeviceLog;
import com.filmi3k.movies.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DeviceLogRepository extends JpaRepository<DeviceLog, Integer> {
    List<DeviceLog> findByUser(User user);

    DeviceLog findByIpAddress(String ip);

    DeviceLog findByUserAndIpAddress(User user, String ip);

    DeviceLog findDeviceLogByUser(User user);
}