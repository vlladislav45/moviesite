package com.filmi3k.movies.services;

import com.filmi3k.movies.MoviesApplication;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.FileParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = MoviesApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {
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
            if(userService.getUserByIpAddress(ip) != null) {
                User user = userService.getUserByIpAddress(ip);

                if (userService.getIsEnabledByUserId(user.getUserId())){
                    assert userService.ban(user,parser) != null;
                    parser.addBannedIPAddress(ip);
                }
            }
        }
    }
}
