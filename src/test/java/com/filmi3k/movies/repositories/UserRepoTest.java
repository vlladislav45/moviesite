package com.filmi3k.movies.repositories;

import com.filmi3k.movies.MoviesApplication;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.entities.UserInfo;
import com.filmi3k.movies.domain.entities.UserPreferences;
import com.filmi3k.movies.domain.entities.UserRole;
import com.filmi3k.movies.repository.api.RoleRepository;
import com.filmi3k.movies.repository.api.UserInfoRepository;
import com.filmi3k.movies.repository.api.UserPreferencesRepository;
import com.filmi3k.movies.repository.api.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
import java.util.*;

@SpringBootTest(classes = MoviesApplication.class)
public class UserRepoTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserPreferencesRepository userPreferencesRepository;

    @Test
    @Transactional
    void addUser() {
        User user = new User("vladislavl3@abv.bg", "vladislavl3", "123456");
        user.setIpAddress("127.0.0.1");
        user.setCreatedTime(user.getDateTimeCreated());

        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        if(this.userRepository.findAll().isEmpty()) {
            List<UserRole> roles = new ArrayList<>();
            roles.add(roleRepository.getUserRoleByAuthority("USER"));
            roles.add(roleRepository.getUserRoleByAuthority("ADMIN"));
            user.setAuthorities(roles);
        }else {
            List<UserRole> roles = new ArrayList<>();
            roles.add(roleRepository.getUserRoleByAuthority("USER"));
            user.setAuthorities(roles);
        }

        this.userRepository.saveAndFlush(user);

        User actual = userRepository.getUserByUsername("vladislavl3");
        Assert.assertEquals("User is not exist", user, actual);

        this.userInfoRepository.saveAndFlush(new UserInfo(user)); //Create the relationship between User and User Info
        this.userPreferencesRepository.saveAndFlush(new UserPreferences(user)); // Create the relationship between User and User Preferences
    }

    @Test
    @Transactional
    void CheckUserIsBannedById() {
        int id = 3;
        Assert.assertTrue(userRepository.isEnabledUser(id));
    }
}
