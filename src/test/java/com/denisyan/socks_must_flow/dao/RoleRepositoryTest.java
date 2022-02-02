package com.denisyan.socks_must_flow.dao;

import com.denisyan.socks_must_flow.dao.RoleRepository;
import com.denisyan.socks_must_flow.entity.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    void addDataForTest(){
        roleRepository.save(new Role("ROLE_TEST"));
    }


    @Test
    void findByName_ExistRoleGiven_ExpectROLE_ROLE_TEST() {
        Role role = roleRepository.findByName("ROLE_TEST");

        assertEquals("ROLE_TEST", role.getName());
    }

    @Test
    void findByName_NotExistRoleGiven_ExpectNull() {
        Role role = roleRepository.findByName("ROLE_NOT_EXIST");

        assertNull(role);
    }
}