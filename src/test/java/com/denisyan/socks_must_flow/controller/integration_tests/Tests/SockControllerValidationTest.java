package com.denisyan.socks_must_flow.controller.integration_tests.Tests;


import com.denisyan.socks_must_flow.Application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class SockControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void incorrectColorParams_addSocks_Expect400BadRequest() throws Exception {

        mockMvc.perform(get("/api/socks?color=INCORRECTCOLOR&operation=equal&cottonPart=50"))
                .andExpect(status().isBadRequest());

    }


}
