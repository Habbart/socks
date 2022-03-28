package com.denisyan.socks_must_flow.dao;

import com.denisyan.socks_must_flow.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeAll
    void addDataToDBForTest(){
        userRepository.save(new User("Petr", "Ivanov", "MEGAPETR", "qwerty12345" ));
    }

    @Test
    void findByLogin_ExistUserGiven_ExpectTestUser() {
        User user = userRepository.findByLogin("MEGAPETR");

        assertEquals("Petr", user.getName());
        assertEquals("Ivanov", user.getSurname());
        assertEquals("qwerty12345", user.getPassword());
    }

    @Test
    void findByLogin_NotExistUserGiven_ExpectNull() {
        User user = userRepository.findByLogin("Not_Exist_User");

        assertNull(user);

    }

    @Test
    void existing_of_superUser() {
        User user = userRepository.findByLogin("admin");
        assertNotNull(user);

    }
}