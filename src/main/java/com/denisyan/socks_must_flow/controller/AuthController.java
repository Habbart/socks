package com.denisyan.socks_must_flow.controller;


import com.denisyan.socks_must_flow.service.UserService;
import com.denisyan.socks_must_flow.validators.jwt.JwtProvider;
import com.denisyan.socks_must_flow.entity.User;
import com.denisyan.socks_must_flow.security.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;

    /**
     * регистрация нового пользователя
     * доступна только для Chief of warehouse.
     * @param user
     * @return
     */
    @PostMapping("/register")
    public User registerUser(@RequestBody @Valid User user){
        return userService.saveUser(user);
    }

    /**
     * Авторизация пользователя.
     * Доступна всем ролям.
     * @param user
     * @return
     */
    @PostMapping("/auth")
    public AuthToken authUser(@RequestBody @Valid User user){
        User confirmedUser = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        String token = jwtProvider.generateToken(confirmedUser.getLogin());
        return new AuthToken(token);
    }

}
