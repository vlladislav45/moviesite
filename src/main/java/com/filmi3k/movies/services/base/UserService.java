package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.models.binding.UserRegisterBindingModel;

import java.util.Set;

public interface UserService {
    boolean add(UserRegisterBindingModel u);

    void delete(User u);

    Set<User> getAllUsers();

    User getByUsername(User u);

    User getById(int id);
}
