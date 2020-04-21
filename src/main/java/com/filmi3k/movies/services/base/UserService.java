package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public interface UserService extends UserDetailsService {
    boolean add(UserRegisterBindingModel u);

    void delete(User u);

    Set<User> getAllUsers();

    User getById(int id);
}
