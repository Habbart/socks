package com.denisyan.socks_must_flow.controller;


import com.denisyan.socks_must_flow.dto.UserDto;
import com.denisyan.socks_must_flow.entity.User;
import com.denisyan.socks_must_flow.security.AuthToken;
import com.denisyan.socks_must_flow.service.UserService;
import com.denisyan.socks_must_flow.validators.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller for Authorization and Authentication
 * For now has two roles - worker and Chief of warehouse
 */
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final ModelMapper modelMapper;

    /**
     * register of new user
     * allowed only for Role Chief of warehouse.
     *
     * @param userDto which should be registered
     * @return userDto which was registered
     */
    @PostMapping("/register")
    public UserDto registerUser(@RequestBody @Valid UserDto userDto) {
        User userToRegister = modelMapper.map(userDto, User.class);
        User registeredUser = userService.saveUser(userToRegister);
        return modelMapper.map(registeredUser, UserDto.class);
    }

    /**
     * Authorization of user.
     * Allowed to everybody.
     * Return auth Bearer security token with expiration date = current date + 10 days
     * All future request should contain this token
     *
     * @param userDto which should be authorizated
     * @return security token which should be added to all future requests
     */
    @PostMapping("/auth")
    public AuthToken authUser(@RequestBody @Valid UserDto userDto) {
        User confirmedUser = userService.findByLoginAndPassword(userDto.getLogin(), userDto.getPassword());
        String token = jwtProvider.generateToken(confirmedUser.getLogin());
        return new AuthToken(token);
    }

}
