package com.denisyan.socks_must_flow.service;


import com.denisyan.socks_must_flow.dao.SocksRepository;
import com.denisyan.socks_must_flow.entity.Sock;
import com.denisyan.socks_must_flow.exception_handler.IllegalParamException;
import com.denisyan.socks_must_flow.exception_handler.SocksNotFound;
import com.denisyan.socks_must_flow.helper.AllowedColors;
import com.denisyan.socks_must_flow.helper.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class SocksService {

    private final static Logger service_logger = LoggerFactory.getLogger("SockService Logger");


    private final SocksRepository socksRepository ;

    public SocksService(SocksRepository socksRepository) {
        this.socksRepository = socksRepository;
    }


    // todo оформить спеку
    //todo больше тестов
    //todo запаковать приложени в докер
    //todo добавить UI с помощью vaadin
    /**
     * Проверяет есть ли носки с таким же цветом и процентным содержанием хлопка в базе.
     * Если есть - то суммирует остатки с переданным количестов.
     * Если нет - то добавляет позицию на склад.
     * возвращает носок, который был сохранен
     * @param sock
     * @return Sock
     */
    public Sock addSocks(Sock sock){
        if(socksRepository.existsByColorAndAndCottonPart(sock.getColor(), sock.getCottonPart())){
            service_logger.info("зашли в if");
            Sock sockFromDB= socksRepository.getByColorAndCottonPartEquals(sock.getColor(), sock.getCottonPart());
            service_logger.info("socks from DB:" + sockFromDB.getColor() + " " + sockFromDB.getQuantity());
            int quantity = sockFromDB.getQuantity() + sock.getQuantity();
            int id = sockFromDB.getId();
            sock.setId(id);
            sock.setQuantity(quantity);
            service_logger.info("sock на сохранение " + sock.getColor() + " " + sock.getQuantity());
            return socksRepository.save(sock);
        } else {
            return socksRepository.save(sock);
        }

    }

    /**
     * Метод проверяет, есть ли носок с таким же цветом и процентным содержанием хлопка в базе.
     * Если данный носок есть на складе - то списывает количество, которое доступно для списания. Может вернуть 0 носков, если носков нет для списания.
     * Если данной позиции нет на складе - то кдиает исключение.
     * Возвращает носок, который был удален
     * @param sock
     * @return Sock
     */
    public Sock removeSocks(Sock sock) {
        if(socksRepository.existsByColorAndAndCottonPart(sock.getColor(), sock.getCottonPart())){
            Sock sockFromDB = socksRepository.getByColorAndCottonPartEquals(sock.getColor(), sock.getCottonPart());
            int quantity = sockFromDB.getQuantity() - sock.getQuantity();
            int id = sockFromDB.getId();
            if(quantity < 0) quantity = 0;
            sock.setId(id);
            sock.setQuantity(quantity);
            return socksRepository.save(sock);
        } else {
            throw new SocksNotFound("No such kind of socks in warehouse");
        }

    }


    /**
     * Возвращает список всех носков которые подходят по цвету, процентному содержанию хлопка в соотвтетсвии с заданым критерием поиска(больше, меньше, равно).
     * Проверяет параметры, если передан неверный цвет, неверное процентное содержание или критерий поиска, то кидает исключение.
     * Если нет ни оного носка вернёт пустой лист.
     * @param color
     * @param operation
     * @param cottonPart
     * @return List<Sock>
     */
    public List<Sock> getAllSocks(String color, String operation, Integer cottonPart) {
        Sock sockForRequest = checkParamsAndReturnSockIfPossible(color, operation, cottonPart);
        String assertion = operation.toLowerCase(Locale.ROOT).trim();
        service_logger.debug("params of URL: " + color + " " + operation + " " + cottonPart);
        if(assertion.equals(Operation.LESS_THAN.getFieldName())){
            List<Sock> result = socksRepository.getSocksByColorAndCottonPartLessThan(sockForRequest.getColor(), sockForRequest.getCottonPart());
            service_logger.debug("result less than: " + result );
            return result;
        } else if(assertion.equals(Operation.MORE_THAN.getFieldName())){
            List<Sock> result =socksRepository.getSocksByColorAndCottonPartGreaterThan(sockForRequest.getColor(), sockForRequest.getCottonPart());
            service_logger.debug("result more than: " + result );
            return result;
        } else {
            Sock result = socksRepository.getByColorAndCottonPartEquals(sockForRequest.getColor(), sockForRequest.getCottonPart());
            service_logger.debug("result equals: "+ result );
            return List.of(result);
        }
    }

    private Sock checkParamsAndReturnSockIfPossible(String color, String operation, Integer cottonPart ){
        checkColorIfAllowed(color);
        if(Arrays.stream(Operation.values()).noneMatch(s -> s.getFieldName().equals(operation.toLowerCase().trim()))) throw new IllegalParamException("Illegal operation param");
        if(cottonPart < 0 || cottonPart > 100 ) throw new IllegalParamException("Illegal cottonPart param");
        return new Sock(color, cottonPart, 0);
    }

    private void checkColorIfAllowed( String color){
        service_logger.debug("проверка цвета " + color);
        if(Arrays.stream(AllowedColors.values()).map(AllowedColors::getFieldName).noneMatch(s -> s.equals(color))) {
            throw new IllegalParamException("color is incorrect, please check color with allowed colors");
        }
    }

}
