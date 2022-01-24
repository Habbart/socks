package com.denisyan.socks_must_flow.exception_handler;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GlobalExceptionHandlerTest {

    GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
         handler = new GlobalExceptionHandler();
    }

    @Test()
    void noSuchSocks_should_return_NoSuchSocksMessage() {
        SocksNotFound socksNotFound = new SocksNotFound("No such socks");

        ResponseEntity<String> stringResponseEntity = handler.NoSuchSocks(socksNotFound);

        String expect = "No such socks";
        assertEquals(expect, stringResponseEntity.getBody());
    }

    @Test()
    void noSuchSocks_should_return_404NoSuchSocks() {
        SocksNotFound socksNotFound = new SocksNotFound("No such socks");

        ResponseEntity<String> stringResponseEntity = handler.NoSuchSocks(socksNotFound);

        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;
        assertEquals(expectedStatus, stringResponseEntity.getStatusCode());
    }

    @Test
    void illegalParamException_should_return_NoSuchParamMessage() {
        IllegalParamException illegalParamException = new IllegalParamException("No such param");

        ResponseEntity<String> stringResponseEntity = handler.IllegalParamException(illegalParamException);

        String expect = "No such param";
        assertEquals(expect, stringResponseEntity.getBody());
    }

    @Test
    void illegalParamException_should_return_badRequest() {
        IllegalParamException illegalParamException = new IllegalParamException("No such param");

        ResponseEntity<String> stringResponseEntity = handler.IllegalParamException(illegalParamException);

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertEquals(expectedStatus, stringResponseEntity.getStatusCode());
    }

    @Test
    void loginAlreadyExistException_should_return_badRequest() {

        LoginAlreadyExistException loginAlreadyExistException = new LoginAlreadyExistException("Login already exist");

        ResponseEntity<String> stringResponseEntity = handler.LoginAlreadyExistException(loginAlreadyExistException);

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertEquals(expectedStatus, stringResponseEntity.getStatusCode());

    }

    @Test
    void loginAlreadyExistException_should_return_LoginAlreadyExistMessage() {

        LoginAlreadyExistException loginAlreadyExistException = new LoginAlreadyExistException("Login already exist");

        ResponseEntity<String> stringResponseEntity = handler.LoginAlreadyExistException(loginAlreadyExistException);

        String expect = "Login already exist";
        assertEquals(expect, stringResponseEntity.getBody());

    }
}