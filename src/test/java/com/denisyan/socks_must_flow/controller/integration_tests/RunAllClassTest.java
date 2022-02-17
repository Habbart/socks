package com.denisyan.socks_must_flow.controller.integration_tests;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@Suite.SuiteClasses({SockControllerTestGetSocks.class, SockControllerTestIncome.class, SocksControllerTestOutcome.class, SockControllerValidationTest.class})
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RunAllClassTest {



}
