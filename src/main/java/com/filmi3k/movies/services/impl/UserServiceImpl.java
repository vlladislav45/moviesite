package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.entities.UserInfo;
import com.filmi3k.movies.domain.entities.UserPreferences;
import com.filmi3k.movies.domain.entities.UserRole;
import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.repository.api.*;
import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.FileParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper map, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, UserPreferencesRepository userPreferencesRepository, UserInfoRepository userInfoRepository) {
        this.userRepository = userRepository;
        this.modelMapper = map;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.userPreferencesRepository = userPreferencesRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public boolean add(UserRegisterBindingModel userRegisterBindingModel) {
        User userEntity = this.modelMapper.map(userRegisterBindingModel, User.class);
        userEntity.setIpAddress("127.0.0.1");
        userEntity.setCreatedTime(userEntity.getDateTimeCreated());

        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));

        //rules
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setEnabled(true);

        if(this.userRepository.findAll().isEmpty()) {
            List<UserRole> roles = new ArrayList<>();
            roles.add(roleRepository.getUserRoleByAuthority("USER"));
            roles.add(roleRepository.getUserRoleByAuthority("ADMIN"));
            userEntity.setAuthorities(roles);
        }else {
            List<UserRole> roles = new ArrayList<>();
            roles.add(roleRepository.getUserRoleByAuthority("USER"));
            userEntity.setAuthorities(roles);
        }

        this.userRepository.saveAndFlush(userEntity);

        this.userInfoRepository.saveAndFlush(new UserInfo(userEntity)); //Create the relationship between User and User Info
        this.userPreferencesRepository.saveAndFlush(new UserPreferences(userEntity)); // Create the relationship between User and User Preferences

        return true;
    }

    @Override
    public void delete(User u) {
        userRepository.delete(u);
    }

    @Override
    public Set<User> getAllUsers() {
        return new HashSet<>(this.userRepository.findAll());
    }

    @Override
    public boolean getUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        if(user != null)
            return true;
        return false;
    }

    @Override
    public boolean getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        if(user != null)
            return true;
        return false;
    }

    @Override
    public User getById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.get();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User foundUser = this.userRepository.getUserByUsername(s);
        if(foundUser != null) {
            return foundUser;
        }else {
            throw new UsernameNotFoundException("Username not found");
        }
    }


    @Override
    public User ban(User user, FileParser fileParser) {
        user.setEnabled(false);
        userRepository.save(user);

        if(fileParser != null) fileParser.addBannedIPAddress(user.getIpAddress()); // To whitelist

        if(!userRepository.isEnabledUser(user.getUserId())) {
            return user;
        }
        return null;
    }

    @Override
    public Set<User> getBannedUsers() {
        return userRepository.getAllByIsEnabled(false);
    }

    @Override
    public User getUserByIpAddress(String ipAddress) {
        return userRepository.getUserByIpAddress(ipAddress);
    }

    @Override
    public boolean isEnabledUser(int id) {
        return userRepository.isEnabledUser(id);
    }

    @Override
    public void banUsersByIP(FileParser fileParser) {
        List<String> bannedIPs = new ArrayList<>();
        try {
            if(fileParser != null) fileParser.parseBannedIPAddresses(bannedIPs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(String ip : bannedIPs) {
            if(userRepository.getUserByIpAddress(ip) != null) {
                User user = userRepository.getUserByIpAddress(ip);

                if (userRepository.isEnabledUser(user.getUserId())){
                    this.ban(user, fileParser);
                }
            }
        }
    }
}
