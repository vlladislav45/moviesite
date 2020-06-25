package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.entities.UserRole;
import com.filmi3k.movies.domain.entities.enums.BanAccount;
import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.repository.api.GenderRepository;
import com.filmi3k.movies.repository.api.RoleRepository;
import com.filmi3k.movies.repository.api.UserRepository;
import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.FileParser;
import com.filmi3k.movies.utils.getIPAddress;
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
    private final GenderRepository genderRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper map, BCryptPasswordEncoder bCryptPasswordEncoder, GenderRepository genderRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = map;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.genderRepository = genderRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean add(UserRegisterBindingModel userRegisterBindingModel) {
        User userEntity = this.modelMapper.map(userRegisterBindingModel, User.class);
        try {
            userEntity.setIpAddress(getIPAddress.getIp());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public User getUserByUsername(String username) {
        User user = userRepository.getUserByUsername(username);
        return user;
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
        user.setEnabled(BanAccount.YES.toBoolean());
        userRepository.save(user);

        if(fileParser != null) fileParser.addBannedIPAddress(user.getIpAddress()); // To whitelist

        if(userRepository.getIsEnabledByUserId(user.getUserId()) == BanAccount.YES.toBoolean()) {
            return user;
        }
        return null;
    }

    @Override
    public Set<User> getBannedUsers() {
        return userRepository.getAllByIsEnabled(BanAccount.YES.toBoolean());
    }

    @Override
    public User getUserByIpAddress(String ipAddress) {
        return userRepository.getUserByIpAddress(ipAddress);
    }

    @Override
    public boolean getIsEnabledByUserId(int id) {
        return userRepository.getIsEnabledByUserId(id);
    }

    @Override
    public void banUsersByIP(FileParser fileParser) {
        List<String> bannedIPs = new ArrayList<>();
        try {
            assert fileParser != null;
            fileParser.parseBannedIPAddresses(bannedIPs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(String ip : bannedIPs) {
            if(userRepository.getUserByIpAddress(ip) != null) {
                User user = userRepository.getUserByIpAddress(ip);

                if (userRepository.getIsEnabledByUserId(user.getUserId())){
                    this.ban(user, fileParser);
                }
            }
        }
    }
}
