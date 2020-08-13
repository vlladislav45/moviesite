package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.DeviceLog;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.models.binding.AuthenticationRequestBindingModel;
import com.filmi3k.movies.repository.api.DeviceLogRepository;
import com.filmi3k.movies.repository.api.UserRepository;
import com.filmi3k.movies.services.base.DeviceLogService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class DeviceLogServiceImpl implements DeviceLogService {
    private final DeviceLogRepository deviceLogRepository;

    public DeviceLogServiceImpl(DeviceLogRepository deviceLogRepository) {
        this.deviceLogRepository = deviceLogRepository;
    }

    @Override
    public List<DeviceLog> findAllByUser(User user) {
        return deviceLogRepository.findByUser(user);
    }

    @Override
    public void addNewDeviceLog(AuthenticationRequestBindingModel authenticationRequest, String jwt, UserDetails userDetails) {

        if(deviceLogRepository.findByUserAndIpAddress(userDetails, authenticationRequest.getIp()) == null) {

            DeviceLog deviceLog = new DeviceLog(authenticationRequest.getUag(),
                    authenticationRequest.getLoc(),
                    authenticationRequest.getIp(),
                    jwt,
                    userDetails);

            deviceLogRepository.saveAndFlush(deviceLog);
        }else {
            DeviceLog deviceLog = deviceLogRepository.findByUserAndIpAddress(userDetails, authenticationRequest.getIp());
            deviceLog.setLastLoggedIn(Timestamp.valueOf(deviceLog.getDateTimeCreated()));
            // We update the last logged time, but we generated new token, so we need to update it in DB
            deviceLog.setJwt(jwt);

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
    public DeviceLog findDeviceLogByUserAndJwt(User user, String jwt) {
        return deviceLogRepository.findDeviceLogByUserAndJwt(user, jwt);
    }
}
