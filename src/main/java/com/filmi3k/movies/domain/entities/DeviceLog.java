package com.filmi3k.movies.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "device_log")
@NoArgsConstructor
@Data
public class DeviceLog extends BaseEntity {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id", nullable = false, unique = true, updatable = false)
    private int deviceId;

    @Column(name = "device_details", nullable = false)
    private String deviceDetails;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "last_logged_in", nullable = false)
    private Timestamp lastLoggedIn;

    @JsonIgnore
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "jwt", nullable = false)
    private String jwt;

    @JsonIgnore
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private UserDetails user;

    public DeviceLog(String deviceDetails, String location,
                     String ipAddress, String jwt, UserDetails user) {
        this.deviceDetails = deviceDetails;
        this.location = location;
        this.lastLoggedIn = Timestamp.valueOf(getDateTimeCreated());
        this.ipAddress = ipAddress;
        this.jwt = jwt;
        this.user = user;
    }
}
