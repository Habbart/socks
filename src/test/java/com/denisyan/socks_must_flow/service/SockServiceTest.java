package com.denisyan.socks_must_flow.service;


import com.denisyan.socks_must_flow.dao.SocksRepository;
import com.denisyan.socks_must_flow.entity.Sock;
import com.denisyan.socks_must_flow.exception_handler.SocksNotFound;
import com.denisyan.socks_must_flow.service.SocksService;
import com.vaadin.pro.licensechecker.Product;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;


import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class SockServiceTest {


        @MockBean
        SocksRepository socksRepository;

        @Autowired
        private SocksService socksService = new SocksService(socksRepository);


        @Test
        void getAllSocks(){


                given(socksRepository.getByColorAndCottonPartEquals("black", 50))
                        .willReturn(new Sock("black", 50, 150)); // проверка для условия equal
                given(socksRepository.getSocksByColorAndCottonPartLessThan("blue", 60))
                        .willReturn(List.of(new Sock("blue", 50, 150),
                                new Sock("blue", 40, 200),
                                new Sock("blue", 30, 100)));//проверка для условия less than
                given(socksRepository.getSocksByColorAndCottonPartGreaterThan("blue", 40))
                        .willReturn(List.of(new Sock("blue", 50, 10),
                                new Sock("blue", 45, 20),
                                new Sock("blue", 75, 30))); // проверка для условия greater Than
                int expectedEqual = 150;
                int expectedLessThan = 450;
                int expectedMoreThan = 60;


                List<Sock> allSocksEqual = socksService.getAllSocks("black", "equal", 50);
                List<Sock> allSocksLessThan = socksService.getAllSocks("blue", "lessThan", 60);
                List<Sock> allSocksMoreThan = socksService.getAllSocks("blue", "moreThan", 40);


                Assertions.assertEquals(expectedEqual, allSocksEqual.get(0).getQuantity());
                Assertions.assertEquals(expectedLessThan, allSocksLessThan.stream().mapToInt(Sock::getQuantity).sum());
                Assertions.assertEquals(expectedMoreThan, allSocksMoreThan.stream().mapToInt(Sock::getQuantity).sum());

        }


        @Test
        void addSocks_socksNotExist(){
                Sock black = new Sock(1, "black", 40, 40);

                socksService.addSocks(black);

                verify(socksRepository).save(black);
        }

        @Test
        void addSocks_socksExist(){
                Sock black = new Sock(1, "black", 40, 40);
                given(socksRepository.existsByColorAndAndCottonPart("black", 40)).willReturn(true);
                given(socksRepository.getByColorAndCottonPartEquals("black", 40))
                        .willReturn(black);


                socksService.addSocks(black);


                verify(socksRepository).existsByColorAndAndCottonPart(black.getColor(), black.getCottonPart());
                Assertions.assertEquals(80, black.getQuantity());
                verify(socksRepository).save(black);
        }

        @Test
        void removeSocks_NotExist_ShouldThrowException() {
                Exception exception = Assert.assertThrows(SocksNotFound.class, () ->{
                        socksService.removeSocks(new Sock("red", 50, 50));
                });

                String expectedMessage = "No such kind of socks in warehouse";
                String actualMessage = exception.getMessage();

                assertTrue(actualMessage.contains(expectedMessage));

        }

        @Test
        void removeSocks_Exist() {
                Sock sockForRemove = new Sock(1, "red", 50, 50);
                given(socksRepository.existsByColorAndAndCottonPart("red", 50)).willReturn(true);
                given(socksRepository.getByColorAndCottonPartEquals("red", 50)).willReturn(new Sock(1, "red", 50, 40));


                socksService.removeSocks(sockForRemove);


                Assertions.assertEquals(0, sockForRemove.getQuantity());
                verify(socksRepository).existsByColorAndAndCottonPart(sockForRemove.getColor(), sockForRemove.getCottonPart());
                verify(socksRepository).save(sockForRemove);
        }




}
