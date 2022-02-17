package com.denisyan.socks_must_flow.controller;


import com.denisyan.socks_must_flow.service.UserService;
import com.denisyan.socks_must_flow.validators.jwt.JwtProvider;
import com.denisyan.socks_must_flow.entity.User;
import com.denisyan.socks_must_flow.security.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller for Authorization and Authentication
 * For now has two roles - worker and Chief of warehouse
 */

@RestController
public class AuthController {

    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;

    /**
     * register of new user
     * allowed only for Role Chief of warehouse.
     * @param user which should be registered
     * @return user which was registered
     */
    @PostMapping("/register")
    public User registerUser(@RequestBody @Valid User user){
        return userService.saveUser(user);
    }

    /**
     * Authorization of user.
     * Allowed to everybody.
     * Return auth Bearer security token with expiration date = current date + 10 days
     * All future request should contain this token
     * @param user which should be authorizated
     * @return security token which should be added to all future requests
     */
    @PostMapping("/auth")
    public AuthToken authUser(@RequestBody @Valid User user){
        User confirmedUser = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        String token = jwtProvider.generateToken(confirmedUser.getLogin());
        return new AuthToken(token);
    }

}
