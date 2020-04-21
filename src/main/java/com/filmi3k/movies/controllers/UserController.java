package com.filmi3k.movies.controllers;

import com.filmi3k.movies.models.binding.UserRegisterBindingModel;
import com.filmi3k.movies.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController extends BaseController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register() { return "register"; }

    @PostMapping("/register")
    public ModelAndView registerConfirm(@ModelAttribute UserRegisterBindingModel userRegisterBindingModel) {
        //(Binding model) that means is binding front-end with our back-end binding model
        if(!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            return view("register");
        }else {
            this.userService.add(userRegisterBindingModel);
            return view("login");
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
