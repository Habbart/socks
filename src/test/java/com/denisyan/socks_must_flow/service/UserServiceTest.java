package com.denisyan.socks_must_flow.service;

import com.denisyan.socks_must_flow.dao.RoleRepository;
import com.denisyan.socks_must_flow.dao.UserRepository;
import com.denisyan.socks_must_flow.entity.User;
import com.denisyan.socks_must_flow.exception_handler.IllegalParamException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


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
        User expected = userService.saveUser(userToSave);
        assertEquals(userToSave, expected);
    }


    @Test
    void findByLogin_expectTestUser_shouldBeFound() {
        User userToReturn = new User("TestUser", "TestUser", "TestUser", "TestUser");
        given(userRepository.findByLogin("TestUser")).willReturn(userToReturn);
        User userToFind = userService.findByLogin("TestUser");
        assertEquals(userToReturn.getLogin(), userToFind.getLogin());

    }


    @Test
    void givenCorrectLoginAndPassword_should_returnUser() {
        User userToReturn = new User("TestUser", "TestUser", "TestUser", "TestUser");
        given(userRepository.findByLogin("TestUser")).willReturn(userToReturn);
        given(passwordEncoder.matches("TestUser", userToReturn.getPassword())).willReturn(true);


        User expected = userService.findByLoginAndPassword("TestUser", "TestUser");

        assertEquals(userToReturn.getLogin(), expected.getLogin());

    }

    @Test
    void givenCorrectLoginAndIncorrectPassword_should_throwException() {
        User userToReturn = new User("TestUser", "TestUser", "TestUser", "TestUser");
        given(userRepository.findByLogin("TestUser")).willReturn(userToReturn);
        given(passwordEncoder.matches("TestUser", userToReturn.getPassword())).willReturn(false);

        Throwable exception = assertThrows(IllegalParamException.class, () -> userService.findByLoginAndPassword("TestUser", "TestUser"));


        assertTrue(exception.getMessage().contains("password"));
    }

    @Test
    void findByLoginAndPassword() {

        given(userRepository.findByLogin("TestUser")).willReturn(null);


        Throwable exception = assertThrows(IllegalParamException.class, () -> userService.findByLoginAndPassword("TestUser", "TestUser"));


        assertTrue(exception.getMessage().contains("username"));
    }
}