package com.filmi3k.movies.controllers;

import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class UserController extends BaseController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register_user")
    public ResponseEntity<String> register(@RequestBody UserRegisterBindingModel userRegisterBindingModel) {
        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            return new ResponseEntity<>(
                    "The password does not match the confirmation password",
                    HttpStatus.OK
            );
        }else {
            this.userService.add(userRegisterBindingModel);
        }
        return new ResponseEntity<>(
                "NULL",
                HttpStatus.OK
        );
    }

    @GetMapping("/register/userAvailable/{username}")
    public ResponseEntity<String> getUsernameIfExist(@PathVariable String username) {
        if(userService.getUserByUsername(username) != null) {
            ResponseEntity<String> usernameAvailable = ResponseEntity.ok()
                    .body("true");
            return usernameAvailable;
        }
        ResponseEntity<String> usernameError = ResponseEntity.ok()
                .body("false");
        return usernameError;
    }

    @GetMapping
    public String getLogin() {
        return "login";
    }
}
