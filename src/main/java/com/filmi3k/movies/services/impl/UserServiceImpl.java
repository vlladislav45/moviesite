package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.entities.Gender;
import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.entities.UserRole;
import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.repositories.api.UserRepository;
import com.filmi3k.movies.services.base.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper map, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = map;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean add(UserRegisterBindingModel userRegisterBindingModel) {
        User userEntity = this.modelMapper.map(userRegisterBindingModel, User.class);
        Gender gender = new Gender();
        gender.setGenderName(userRegisterBindingModel.getGender());
        userEntity.setGender(gender);
        userEntity.setCreatedTime(userEntity.getDateCreated());

        //Review and image for user


        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));

        if(this.userRepository.findAll().isEmpty()) {
            Set<UserRole> roles = new HashSet<>();
            roles.add(new UserRole("USER"));
            roles.add(new UserRole("ADMIN"));
            userEntity.setRoles(roles);
        }else {
            Set<UserRole> roles = new HashSet<>();
            roles.add(new UserRole("USER"));
            userEntity.setRoles(roles);
        }

        this.userRepository.save(userEntity);
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
    public User getByUsername(User u) {
        User foundUser = this.userRepository.getUserByUsername(u.getUsername());
        if(foundUser != null) {
            return foundUser;
        }else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

    @Override
    public User getById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.get();
    }


}
