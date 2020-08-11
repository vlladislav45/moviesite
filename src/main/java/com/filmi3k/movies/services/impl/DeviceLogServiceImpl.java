package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.DeviceLog;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.models.binding.AuthenticationRequestBindingModel;
import com.filmi3k.movies.repository.api.DeviceLogRepository;
import com.filmi3k.movies.repository.api.UserRepository;
import com.filmi3k.movies.services.base.DeviceLogService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class DeviceLogServiceImpl implements DeviceLogService {
    private final DeviceLogRepository deviceLogRepository;
    private final UserRepository userRepository;

    public DeviceLogServiceImpl(DeviceLogRepository deviceLogRepository, UserRepository userRepository) {
        this.deviceLogRepository = deviceLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<DeviceLog> findAllByUser(User user) {
        return deviceLogRepository.findByUser(user);
    }

    @Override
    public void addNewDeviceLog(AuthenticationRequestBindingModel authenticationRequest, String jwt) {
        User user = userRepository.getUserByUsername(authenticationRequest.getUsername());

        if(deviceLogRepository.findByUserAndIpAddress(user, authenticationRequest.getIp()) == null) {

            DeviceLog deviceLog = new DeviceLog(authenticationRequest.getUag(),
                    authenticationRequest.getLoc(),
                    authenticationRequest.getIp(),
                    jwt,
                    user);

            deviceLogRepository.saveAndFlush(deviceLog);
        }else {
            DeviceLog deviceLog = deviceLogRepository.findByUserAndIpAddress(user, authenticationRequest.getIp());
            deviceLog.setLastLoggedIn(Timestamp.valueOf(deviceLog.getDateTimeCreated()));

            deviceLogRepository.saveAndFlush(deviceLog);
        }
    }

    @Override
    public void delete(DeviceLog deviceLog) {
        deviceLogRepository.delete(deviceLog);
    }

    @Override
    public DeviceLog findByUserAndIpAddress(User user, String ip) {
        return deviceLogRepository.findByUserAndIpAddress(user, ip);
    }

    @Override
    public DeviceLog findBypAddress(String ip) {
        return deviceLogRepository.findByIpAddress(ip);
    }

    @Override
    public DeviceLog findDeviceLogByUser(User user) {
        return deviceLogRepository.findDeviceLogByUser(user);
    }
}
