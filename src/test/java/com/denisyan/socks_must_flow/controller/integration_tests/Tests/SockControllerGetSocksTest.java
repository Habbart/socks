package com.denisyan.socks_must_flow.controller.integration_tests.Tests;



import com.denisyan.socks_must_flow.Application;
import com.denisyan.socks_must_flow.controller.integration_tests.TestHelper;
import com.denisyan.socks_must_flow.repositories.SocksRepository;
import com.denisyan.socks_must_flow.entity.Sock;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class SockControllerGetSocksTest {

    private static final Logger logger = LoggerFactory.getLogger("SockControllerTestGetSocks");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SocksRepository socksRepository;

    @BeforeEach
    public void clearDB(){
        logger.info("удаляем носки");
        socksRepository.deleteAll();
    }

    @Test
    void givenSocks_whenPostSockAndGetSock_thenStatus200() throws Exception {
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
        int expected = 40;

        Assertions.assertSame(expected, actual, "Возвращается не правильный результат при запросе GET /api/socks");
    }

    @Test
    void givenIncorrectCottonPart_whenGetSocks_thenStatus400() throws Exception {

        mockMvc.perform(get("/api/socks?color=red&operation=moreThan&cottonPart=110")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/socks?color=red&operation=moreThan&cottonPart=110")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    void givenIncorrectColor_whenGetSocks_thenStatus400() throws Exception {

        mockMvc.perform(get("/api/socks?color=ssssss&operation=moreThan&cottonPart=80")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    void givenIncorrectOperation_whenGetSocks_thenStatus400() throws Exception {

        mockMvc.perform(get("/api/socks?color=red&operation=aaaaa&cottonPart=80")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void givenEmptyURL_whenGetSocks_thenStatus400() throws Exception {

        mockMvc.perform(get("/api/socks?")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

}
