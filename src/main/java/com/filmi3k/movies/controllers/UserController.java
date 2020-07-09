package com.filmi3k.movies.controllers;

import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register_user")
    public ResponseEntity<String> register(@RequestBody UserRegisterBindingModel userRegisterBindingModel) {
        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            return ResponseEntity.ok().body("The password does not match the confirmation password");
        }else {
            if(userService.getUserByUsername(userRegisterBindingModel.getUsername()) || userService.getUserByEmail(userRegisterBindingModel.getEmail()))
                return ResponseEntity.ok().body("This user is already registered");
            this.userService.add(userRegisterBindingModel);
        }
        return ResponseEntity.ok().body("Your successfully register an account");
    }

    @GetMapping("/register/userAvailable/{username}")
    public ResponseEntity<String> getUsernameIfExist(@PathVariable String username) {
        if(userService.getUserByUsername(username)) {
            return ResponseEntity.ok()
                    .body("true");
        }
        return ResponseEntity.ok()
                .body("false");
    }

    @GetMapping
    public String getLogin() {
        return "login";
    }
}
