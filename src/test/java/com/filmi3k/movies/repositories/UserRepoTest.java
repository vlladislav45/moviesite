package com.filmi3k.movies.repositories;

import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.entities.UserRole;
import com.filmi3k.movies.repository.api.GenderRepository;
import com.filmi3k.movies.repository.api.RoleRepository;
import com.filmi3k.movies.repository.api.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
import java.util.*;

@SpringBootTest
public class UserRepoTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @Transactional
    void testHashSet() {
        System.out.println(userRepository.getUserByUsername("vladislavl2a"));
    }

    @Test
    @Transactional
    void testUserRepo() {
        User user = new User();
        user.setUsername("vladislavl2a");
        user.setEmail("vladislavl2a@abv.bg");
        user.setGender(genderRepository.findByGenderName("male"));
        user.setCreatedTime(user.getDateCreated());

        user.setPassword(this.bCryptPasswordEncoder.encode("123456"));

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

        User actual = userRepository.getUserByUsername("vladislavl2a");
        Assert.assertEquals("User is not exist", user, actual);
    }
}
