package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.DeviceLog;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.models.binding.AuthenticationRequestBindingModel;

import java.util.*;

public interface DeviceLogService {
    List<DeviceLog> findAllByUser(User user);

    void addNewDeviceLog(AuthenticationRequestBindingModel authenticationRequest, String jwt);

    void delete(DeviceLog deviceLog);

    DeviceLog findByUserAndIpAddress(User user, String ip);

    DeviceLog findBypAddress(String ip);

    DeviceLog findDeviceLogByUser(User user);
}