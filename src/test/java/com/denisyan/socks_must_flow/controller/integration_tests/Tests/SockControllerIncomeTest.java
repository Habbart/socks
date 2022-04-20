package com.denisyan.socks_must_flow.controller.integration_tests.Tests;



import com.denisyan.socks_must_flow.Application;
import com.denisyan.socks_must_flow.controller.integration_tests.TestHelper;
import com.denisyan.socks_must_flow.repositories.SocksRepository;
import com.denisyan.socks_must_flow.entity.Sock;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class SockControllerIncomeTest {

    private final Logger logger = LoggerFactory.getLogger("SockControllerTestIncome");


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
    void givenSocks_whenPostSockIncome_thenStatus200() throws Exception {
        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_50_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void givenOutOfRangeCottonPart_whenPostSockIncome_thenStatus400() throws Exception {
        mockMvc.perform(post("/api/socks/income").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_OUT_OF_RANGE_COTTON_PART_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void givenColorNotExist_whenPostSockIncome_thenStatus400() throws Exception {
        mockMvc.perform(post("/api/socks/income").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_COLOR_NOT_EXIST_50_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenEmptyColorAndZeroQuantity_whenPostSockIncome_thenStatus400() throws Exception {
        mockMvc.perform(post("/api/socks/income").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_EMPTY_COLOR_AND_ZERO_QUANTITY))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNegativeQuantity_whenPostSockIncome_thenStatus400() throws Exception {
        mockMvc.perform(post("/api/socks/income").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_50_NEGATIVE_QUANTITY_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenEmptyBody_whenPostSockIncome_thenStatus400() throws Exception {
        mockMvc.perform(post("/api/socks/income").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_EMPTY_BODY))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenZeroQuantity_whenPostSockIncome_thenStatus200() throws Exception {
        mockMvc.perform(post("/api/socks/income").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_50_ZERO_QUANTITY_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenTwoJSON_whenPostSockAndGetSock_thenStatus200() throws Exception {
        mockMvc.perform(post("/api/socks/income").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_50_JSON))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/socks/income").
                        contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelper.SOCK_GREEN_50_JSON))
                .andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(get("/api/socks?color=green&operation=equal&cottonPart=50")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String jsonString = result.getResponse().getContentAsString();
        ObjectMapper mapper =  new ObjectMapper();
        List<Sock> sock = mapper.readValue(jsonString, new TypeReference<List<Sock>>() {
        });
        int actual = sock.get(0).getQuantity();
        int expected = 80;

        assertEquals(expected, actual, "Возвращается не правильный результат при запросе GET /api/socks/ , также возможно неправлиьное суммирование при приходе носков");
    }
}
