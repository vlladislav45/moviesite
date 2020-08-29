package com.jmovies.domain.models.view;

import com.jmovies.domain.entities.DeviceLog;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DeviceLogViewModel {
    private String deviceDetails;
    private String location;
    private Long lastLoggedIn;
    private String ipAddress;

    public static DeviceLogViewModel toViewModel(DeviceLog deviceLog) {
        DeviceLogViewModel deviceLogViewModel = new DeviceLogViewModel();
        deviceLogViewModel.deviceDetails = deviceLog.getDeviceDetails();
        deviceLogViewModel.location = deviceLog.getLocation();
        deviceLogViewModel.lastLoggedIn = deviceLog.getLastLoggedIn().getTime();
        deviceLogViewModel.ipAddress = deviceLog.getIpAddress();

        return deviceLogViewModel;
    }
}
