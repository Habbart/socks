package com.denisyan.socks_must_flow.controller.integration_tests.Tests;

import com.denisyan.socks_must_flow.Application;
import com.denisyan.socks_must_flow.controller.integration_tests.TestHelper;
import com.denisyan.socks_must_flow.dao.SocksRepository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.Assert.assertSame;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class SocksControllerTestOutcome {

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
    public void givenSocks_whenPostSockOutcome_thenStatus400() throws Exception {

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
    public void givenSocks_whenPostSockOutcome_thenStatus200() throws Exception {
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
    public void givenOutOfRangeCottonPart_whenPostSockOutcome_thenStatus400() throws Exception {
        mockMvc.perform(post("/api/socks/outcome").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_OUT_OF_RANGE_COTTON_PART_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void givenColorNotExist_whenPostSockOutcome_thenStatus400() throws Exception {
        mockMvc.perform(post("/api/socks/outcome").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_COLOR_NOT_EXIST_50_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenEmptyColorAndZeroQuantity_whenPostSockOutcome_thenStatus400() throws Exception {
        mockMvc.perform(post("/api/socks/outcome").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_EMPTY_COLOR_AND_ZERO_QUANTITY))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNegativeQuantity_whenPostSockOutcome_thenStatus400() throws Exception {
        mockMvc.perform(post("/api/socks/outcome").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_50_NEGATIVE_QUANTITY_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenEmptyBody_whenPostSockOutcome_thenStatus400() throws Exception {
        mockMvc.perform(post("/api/socks/outcome").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_EMPTY_BODY))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenZeroQuantity_whenPostSockOutcome_thenStatus200() throws Exception {

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