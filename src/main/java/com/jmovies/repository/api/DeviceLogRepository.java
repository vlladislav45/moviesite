package com.jmovies.repository.api;

import com.jmovies.domain.entities.DeviceLog;
import com.jmovies.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DeviceLogRepository extends JpaRepository<DeviceLog, Integer> {
    List<DeviceLog> findByUser(User user);

    DeviceLog findByIpAddress(String ip);

    DeviceLog findByUserAndIpAddress(UserDetails user, String ip);

    DeviceLog findDeviceLogByUserAndJwt(User user, String jwt);
}
