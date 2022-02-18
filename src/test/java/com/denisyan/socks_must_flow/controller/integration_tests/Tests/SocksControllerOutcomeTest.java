package com.denisyan.socks_must_flow.controller.integration_tests.Tests;

import com.denisyan.socks_must_flow.Application;
import com.denisyan.socks_must_flow.controller.integration_tests.TestHelper;
import com.denisyan.socks_must_flow.dao.SocksRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.Assert.assertSame;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class SocksControllerOutcomeTest {

    private final static Logger logger = LoggerFactory.getLogger("SocksControllerTestOutcome");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    SocksRepository socksRepository;

    @BeforeEach
    public void clearDB(){
        logger.info("удаляем носки");
        socksRepository.deleteAll();
    }

    @Test
    void givenSocks_whenPostSockOutcome_thenStatus200_1() throws Exception {

        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_RED_90_JSON_Q50))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_RED_90_JSON_Q100))
                .andExpect(status().isOk());
    }

    @Test
    void givenSocks_whenPostSockOutcome_thenStatus200() throws Exception {
        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_50_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/socks/outcome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_50_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenOutOfRangeCottonPart_whenPostSockOutcome_thenStatus404() throws Exception {
        mockMvc.perform(post("/api/socks/outcome").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_OUT_OF_RANGE_COTTON_PART_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void givenColorNotExist_whenPostSockOutcome_thenStatus404() throws Exception {
        mockMvc.perform(post("/api/socks/outcome").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_COLOR_NOT_EXIST_50_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenEmptyColorAndZeroQuantity_whenPostSockOutcome_thenStatus404() throws Exception {
        mockMvc.perform(post("/api/socks/outcome").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_EMPTY_COLOR_AND_ZERO_QUANTITY))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNegativeQuantity_whenPostSockOutcome_thenStatus404() throws Exception {
        mockMvc.perform(post("/api/socks/outcome").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_50_NEGATIVE_QUANTITY_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenEmptyBody_whenPostSockOutcome_thenStatus404() throws Exception {
        mockMvc.perform(post("/api/socks/outcome").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_EMPTY_BODY))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenZeroQuantity_whenPostSockOutcome_thenStatus200() throws Exception {

        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_50_JSON))
                .andExpect(status().isOk());


        mockMvc.perform(post("/api/socks/outcome").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_50_ZERO_QUANTITY_JSON))
                .andExpect(status().isOk());
    }

}