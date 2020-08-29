package com.jmovies.services;

import com.jmovies.MoviesApplication;
import com.jmovies.repository.api.DeviceLogRepository;
import com.jmovies.services.base.UserService;
import com.jmovies.utils.FileParser;
import com.jmovies.domain.entities.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = MoviesApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
    @Autowired
    private DeviceLogRepository deviceLogRepository;

    @Autowired
    private UserService userService;

    @Test
    @Transactional
    public void banUsersByIP() {
        FileParser parser = FileParser.initialize();
        List<String> bannedIPs = new ArrayList<>();
        try {
             Assert.assertNotNull(parser);
             parser.parseBannedIPAddresses(bannedIPs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(String ip : bannedIPs) {
            if(deviceLogRepository.findByIpAddress(ip) != null) {
                User user = (User) deviceLogRepository.findByIpAddress(ip).getUser();

                if (userService.isEnabledUser(user.getUserId())){
                    assert userService.ban(user,parser) != null;
                }
            }
        }
        parser.addBannedIPAddress(bannedIPs);
    }
}
