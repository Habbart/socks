package com.denisyan.socks_must_flow.controller.integration_tests;



import com.denisyan.socks_must_flow.controller.integration_tests.Tests.SockControllerTestGetSocks;
import com.denisyan.socks_must_flow.controller.integration_tests.Tests.SockControllerTestIncome;
import com.denisyan.socks_must_flow.controller.integration_tests.Tests.SockControllerValidationTest;
import com.denisyan.socks_must_flow.controller.integration_tests.Tests.SocksControllerTestOutcome;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
//@Suite.SuiteClasses({SockControllerTestGetSocks.class, SockControllerTestIncome.class, SocksControllerTestOutcome.class, SockControllerValidationTest.class})
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class RunAllTestsClass {

    @Test
    public void contextLoad(){
    }



}
