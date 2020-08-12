package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.DeviceLog;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.models.binding.AuthenticationRequestBindingModel;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public interface DeviceLogService {
    List<DeviceLog> findAllByUser(User user);

    void addNewDeviceLog(AuthenticationRequestBindingModel authenticationRequest, String jwt, UserDetails userDetails);

    void delete(DeviceLog deviceLog);

    DeviceLog findByUserAndIpAddress(User user, String ip);

    DeviceLog findBypAddress(String ip);

    DeviceLog findDeviceLogByUserAndJwt(User user, String jwt);
}