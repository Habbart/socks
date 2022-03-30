package com.denisyan.socks_must_flow.service;


import com.denisyan.socks_must_flow.dao.SocksRepository;
import com.denisyan.socks_must_flow.entity.Sock;
import com.denisyan.socks_must_flow.exception_handler.IllegalParamException;
import com.denisyan.socks_must_flow.exception_handler.SocksNotFound;
import com.denisyan.socks_must_flow.validators.AllowedOperation;
import com.denisyan.socks_must_flow.validators.color_validator.AllowedColors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Sock service contain main logic of add, remove and get quantity of socks
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SocksService {

    private final SocksRepository socksRepository;

    //todo добавить проверку сесурити на морду

    /**
     * Check if there are sock with same color and percentage of cotton part in repository
     * If yes - get allowed quantity and add quantity from parameter and save into repo
     * If no - create new sock and add to repository
     * Return sock which was saved
     *
     * @param sock which you want to add
     * @return Sock which was added
     */
    public Sock addOrUpdateSocks(Sock sock) {
        checkColorIfAllowed(sock.getColor());
        if (socksRepository.existsByColorAndAndCottonPart(sock.getColor().toLowerCase(Locale.ROOT), sock.getCottonPart())) {
            log.info("зашли в if");
            Sock sockFromDB = socksRepository.getByColorAndCottonPartEquals(sock.getColor().toLowerCase(Locale.ROOT), sock.getCottonPart());
            log.info(String.format("socks from DB: %s, %d.", sockFromDB.getColor(), sockFromDB.getQuantity()));
            int quantity = sockFromDB.getQuantity() + sock.getQuantity();
            long id = sockFromDB.getId();
            sock.setId(id);
            sock.setQuantity(quantity);
            log.info("sock на сохранение " + sock.getColor() + " " + sock.getQuantity());
            return socksRepository.save(sock);
        } else {
            return socksRepository.save(sock);
        }

    }

    /**
     * Method check if there are sock with same color and percentage of cotton into repo
     * If yes - remove quantity which allowed for remove. Can return 0 socks if there are no socks in repo but repo has this kind of sock
     * If no - throw Socks Not Found exception
     * return sock which was removed
     *
     * @param sock which you want to remove
     * @return Sock which was removed
     */
    public Sock restRemoveSocks(Sock sock) {
        if (socksRepository.existsByColorAndAndCottonPart(sock.getColor(), sock.getCottonPart())) {
            Sock sockFromDB = socksRepository.getByColorAndCottonPartEquals(sock.getColor(), sock.getCottonPart());
            int quantity = sockFromDB.getQuantity() - sock.getQuantity();
            long id = sockFromDB.getId();
            if (quantity < 0) quantity = 0;
            sock.setId(id);
            sock.setQuantity(quantity);
            return socksRepository.save(sock);
        } else {
            throw new SocksNotFound("No such kind of socks in warehouse");
        }

    }

    /**
     * Return list of all socks which suits with params (color, percentage of cotton part, operation filter)
     * If there are no socks with this filter - return empty list
     *
     * @param color      of sock
     * @param operation  - lessThan, equals or moreThan
     * @param cottonPart of sock
     * @return List<Sock>
     */
    public List<Sock> restGetAllSocksByColorAndOperation(String color, String operation, Integer cottonPart) {
        Sock sockForRequest = checkParamsAndReturnSockIfPossible(color, operation, cottonPart);
        String assertion = operation.toLowerCase(Locale.ROOT).trim();
        log.debug("params of URL: " + color + " " + operation + " " + cottonPart);
        if (assertion.equals(AllowedOperation.LESS_THAN.getFieldName())) {
            List<Sock> result = socksRepository.getSocksByColorAndCottonPartLessThan(sockForRequest.getColor(), sockForRequest.getCottonPart());
            log.debug("result less than: " + result);
            return result;
        } else if (assertion.equals(AllowedOperation.MORE_THAN.getFieldName())) {
            List<Sock> result = socksRepository.getSocksByColorAndCottonPartGreaterThan(sockForRequest.getColor(), sockForRequest.getCottonPart());
            log.debug("result more than: " + result);
            return result;
        } else {
            Sock result = socksRepository.getByColorAndCottonPartEquals(sockForRequest.getColor(), sockForRequest.getCottonPart());
            log.debug("result equals: " + result);
            return List.of(result);
        }
    }

    //todo ограничить вывод и описать методы
    public Collection<Sock> findAll() {
        return socksRepository.findAll();
    }

    public Sock add(Sock sock) {
        return socksRepository.save(sock);
    }

    public Sock update(Sock sock) {
        return socksRepository.save(sock);
    }

    public void delete(Sock sock) {
        socksRepository.delete(sock);
    }

    public Collection<Sock> findByColor(String color) {
        return socksRepository.findByColor(color.toLowerCase(Locale.ROOT));
    }

    public Optional<Sock> findById(Long id) {
        return socksRepository.findById(id);
    }

    /**
     * Check if params are correct.
     * Throw exception if they don't
     *
     * @param color      of sock
     * @param operation  lessThan, equals or moreThan
     * @param cottonPart of sock
     * @return sock if it can be created
     */
    private Sock checkParamsAndReturnSockIfPossible(String color, String operation, Integer cottonPart) {
        checkColorIfAllowed(color);
        if (Arrays.stream(AllowedOperation.values()).noneMatch(s -> s.getFieldName().equals(operation.toLowerCase().trim())))
            throw new IllegalParamException("Illegal operation param");
        if (cottonPart < 0 || cottonPart > 100) throw new IllegalParamException("Illegal cottonPart param");
        return new Sock(color, cottonPart, 0);
    }

    /**
     * Check if color are valid and can be found in Allowed Colors Enum
     *
     * @param color of sock
     */
    private void checkColorIfAllowed(String color) {
        log.debug("проверка цвета " + color);
        if(color == null) throw new IllegalArgumentException("Color can't be empty");
        String lowerColor = color.toLowerCase(Locale.ROOT);
        if (Arrays.stream(AllowedColors.values()).map(AllowedColors::getFieldName).noneMatch(s -> s.equals(lowerColor))) {
            throw new IllegalParamException("color is incorrect, please check color with allowed colors");
        }
    }

}
