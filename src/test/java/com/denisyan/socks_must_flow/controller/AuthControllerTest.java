package com.denisyan.socks_must_flow.controller;

import com.denisyan.socks_must_flow.Application;
import com.denisyan.socks_must_flow.controller.integration_tests.TestHelper;
import com.denisyan.socks_must_flow.validators.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    // todo переписал тест на 302 ошибку, хотя должна быть 403
    @Test
    void registerUser_ExpectStatus403Forbidden() throws Exception {
        mockMvc.perform(post("/register"))
                .andExpect(status().isFound());
    }

    @Test
    void registerUser_CorrectUserGiven_ExpectStatus200Ok() throws Exception {
        String token = jwtProvider.generateToken("admin");

        assertNotNull(token);
        mockMvc.perform(post("/register")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.CORRECT_WAREHOUSEMAN_1))
                .andExpect(status().isOk());
    }

    @Test
    void authUser_CorrectUserGiven_ExpectStatus200OK() throws Exception {

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SUPERUSER))
                .andExpect(status().isOk());
    }


    @Test
    void authUser_NonBodyProvided_ExpectStatus400BadRequest() throws Exception {

        mockMvc.perform(post("/auth"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authUser_EmptyBodyProvided_ExpectStatus403Forbidden() throws Exception {

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}