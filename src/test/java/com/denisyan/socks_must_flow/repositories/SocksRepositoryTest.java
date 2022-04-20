package com.denisyan.socks_must_flow.repositories;

import com.denisyan.socks_must_flow.entity.Sock;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SocksRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger("SockRepo Logger");

    @Autowired
    private SocksRepository socksRepository;

    @BeforeAll
    void addSocksForTest(){
        logger.info("удаляем все носки");
        socksRepository.deleteAll();

        socksRepository.save(new Sock("black", 50, 100));
        socksRepository.save(new Sock("black", 30, 50));
        socksRepository.save(new Sock("black", 10, 25));
        socksRepository.save(new Sock("green", 60, 100));
        socksRepository.save(new Sock("red", 70, 75));
        socksRepository.save(new Sock("red", 60, 100));
        socksRepository.save(new Sock("red", 80, 200));
        socksRepository.save(new Sock("blue", 80, 100));

    }

    @AfterAll
    void clearDataBase(){
        logger.info("удаляем все носки");
        socksRepository.deleteAll();
    }


    @Test
    void existsByColorAndAndCottonPart_ExistSockGiven_ExpectTrue() {

        boolean result = socksRepository.existsByColorAndAndCottonPart("black", 50);

        assertTrue(result);
    }
    @Test
    void existsByColorAndAndCottonPart_NotExistCottonPartGiven_ExpectFalse() {
        boolean result = socksRepository.existsByColorAndAndCottonPart("blue", 30);

        assertFalse(result);
    }

    @Test
    void existsByColorAndAndCottonPart_NotExistColorGiven_ExpectFalse() {
        boolean result = socksRepository.existsByColorAndAndCottonPart("aaaaa", 90);

        assertFalse(result);
    }

    @Test
    void getByColorAndCottonPartEquals_ExistSockGiven_ExpectGreen_60_100pcs() {
        Sock sock = socksRepository.getByColorAndCottonPartEquals("green", 60);
        assertEquals(60, sock.getCottonPart());
        assertEquals("green" ,sock.getColor());
        assertEquals(100, sock.getQuantity(), 100);
    }

    @Test
    void getByColorAndCottonPartEquals_NotExistCottonPartGiven_Expect_null() {
        Sock sock = socksRepository.getByColorAndCottonPartEquals("green", 90);
        assertNull(sock);
    }

    @Test
    void getByColorAndCottonPartEquals_NotExistColorGiven_Expect_null() {
        Sock sock = socksRepository.getByColorAndCottonPartEquals("aaaaa", 50);
        assertNull(sock);
    }



    @Test
    void getSocksByColorAndCottonPartGreaterThan_ExistSockGiven_ExpectRed_275pcs() {
        List<Sock> redSocks = socksRepository.getSocksByColorAndCottonPartGreaterThan("red", 60);

        assertEquals("red", redSocks.get(0).getColor());
        assertEquals(275, redSocks.stream().mapToInt(Sock::getQuantity).sum());

    }

    @Test
    void getSocksByColorAndCottonPartGreaterThan_ExistSockGiven_ExpectRed_375pcs() {
        List<Sock> redSocks = socksRepository.getSocksByColorAndCottonPartGreaterThan("red", 50);

        assertEquals("red", redSocks.get(0).getColor());
        assertEquals(375, redSocks.stream().mapToInt(Sock::getQuantity).sum());

    }

    @Test
    void getSocksByColorAndCottonPartGreaterThan_NotExistCottonPartGiven_ExpectEmptyList() {
        List<Sock> redSocks = socksRepository.getSocksByColorAndCottonPartGreaterThan("red", 90);

        assertEquals(0, redSocks.size());
    }

    @Test
    void getSocksByColorAndCottonPartGreaterThan_NotExistColorGiven_ExpectEmptyList() {
        List<Sock> redSocks = socksRepository.getSocksByColorAndCottonPartGreaterThan("aaaaa", 50);

        assertEquals(0, redSocks.size());
    }

    @Test
    void getSocksByColorAndCottonPartLessThan_ExistSockGiven_Expect75pcs() {
        List<Sock> blackSocks = socksRepository.getSocksByColorAndCottonPartLessThan("black", 50);

        assertEquals(75,blackSocks.stream().mapToInt(Sock::getQuantity).sum());

    }
    @Test
    void getSocksByColorAndCottonPartLessThan_ExistSockGiven_Expect175pcs() {
        List<Sock> blackSocks = socksRepository.getSocksByColorAndCottonPartLessThan("black", 60);

        assertEquals(175,blackSocks.stream().mapToInt(Sock::getQuantity).sum());

    }
    @Test
    void getSocksByColorAndCottonPartLessThan_NotExistCottonPartGiven_ExpectEmptyList() {

        List<Sock> blackSocks = socksRepository.getSocksByColorAndCottonPartLessThan("black", 10);

        assertEquals(0, blackSocks.size());
    }
    @Test
    void getSocksByColorAndCottonPartLessThan_NotExistColorGiven_ExpectEmptyList() {

        List<Sock> blackSocks = socksRepository.getSocksByColorAndCottonPartLessThan("aaaaa", 50);

        assertEquals(0, blackSocks.size());
    }

}