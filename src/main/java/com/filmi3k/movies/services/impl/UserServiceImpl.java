package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.entities.UserRole;
import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.repositories.api.GenderRepository;
import com.filmi3k.movies.repositories.api.RoleRepository;
import com.filmi3k.movies.repositories.api.UserRepository;
import com.filmi3k.movies.services.base.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        userEntity.setGender(genderRepository.findByGenderName(userRegisterBindingModel.getGender()));
        userEntity.setCreatedTime(userEntity.getDateCreated());

        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));

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
}
