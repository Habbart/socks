package com.denisyan.socks_must_flow.service;

import com.denisyan.socks_must_flow.dao.RoleRepository;
import com.denisyan.socks_must_flow.dao.UserRepository;
import com.denisyan.socks_must_flow.entity.User;
import com.denisyan.socks_must_flow.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class UserServiceTest {

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    void saveUser_CorrectUserGiven_ShouldReturnSameUser() {
        User userToSave = new User("TestUser", "TestUser", "TestUser", "TestUser");
        given(userRepository.save(userToSave)).willReturn(userToSave);
        User userServiceShouldReturn = userService.saveUser(userToSave);
        assertEquals(userToSave, userServiceShouldReturn);
    }

    @Test
    void saveUser_userAlreadyExistGiven_ShouldThrowIllegalParamException() {
        User userToSave = new User("TestUser", "TestUser", "TestUser", "TestUser");
        given(userRepository.save(userToSave)).willReturn(userToSave);
        User userServiceShouldReturn = userService.saveUser(userToSave);
        assertEquals(userToSave, userServiceShouldReturn);
    }


    @Test
    void findByLogin() {
        //todo дописать тесты
    }

    @Test
    void findByLoginAndPassword() {
        //todo дописать тесты
    }
}