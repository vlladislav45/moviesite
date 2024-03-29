package com.jmovies.repositories;

import com.jmovies.MoviesApplication;
import com.jmovies.domain.entities.User;
import com.jmovies.domain.entities.UserRole;
import com.jmovies.repository.api.RoleRepository;
import com.jmovies.repository.api.UserInfoRepository;
import com.jmovies.repository.api.UserPreferencesRepository;
import com.jmovies.repository.api.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
import java.sql.Timestamp;
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

        user.setCreatedTime(Timestamp.valueOf(user.getDateTimeCreated()));

        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));

        if(this.userRepository.findAll().isEmpty()) {
            Set<UserRole> roles = new HashSet<>(Set.of(roleRepository.getUserRoleByAuthority("USER"), roleRepository.getUserRoleByAuthority("ADMIN")));
            user.setAuthorities(roles);
        }else {
            user.setAuthorities(Set.of(roleRepository.getUserRoleByAuthority("USER")));
        }

        //this.userRepository.saveAndFlush(user);
        User actual = userRepository.getUserByUsername("vladislavl3");
        //Assert.assertEquals("User is not exist", user, actual);

        //this.userInfoRepository.saveAndFlush(new UserInfo(user)); //Create the relationship between User and User Info
        //this.userPreferencesRepository.saveAndFlush(new UserPreferences(user)); // Create the relationship between User and User Preferences
    }

    @Test
    @Transactional
    void CheckUserIsBannedById() {
        int id = 3;
        Assert.assertTrue(userRepository.isEnabledUser(id));
    }
}
