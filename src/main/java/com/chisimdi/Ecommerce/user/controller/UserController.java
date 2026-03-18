package com.chisimdi.Ecommerce.user.controller;

import com.chisimdi.Ecommerce.user.models.UserDTO;
import com.chisimdi.Ecommerce.user.services.UserService;
import com.chisimdi.Ecommerce.user.utils.LoginResponse;
import com.chisimdi.Ecommerce.user.utils.LoginUtil;
import com.chisimdi.Ecommerce.user.utils.RegisterUserUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody @Valid RegisterUserUtil util){
        return userService.registerUser(util);
    }

    @PostMapping("/login")
    public LoginResponse logIn(@RequestBody @Valid LoginUtil util, HttpServletResponse response){
        return userService.logIn(util, response);
    }

    @GetMapping("/profiles")
    public UserDTO viewProfile(){
        return userService.viewProfile();
    }
}
