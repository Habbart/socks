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


/**
 * Sock service contain main logic of add, remove and get quantity of socks
 */

@Service
public class SocksService {

    private final Logger serviceLogger = LoggerFactory.getLogger("SockService Logger");


    private final SocksRepository socksRepository ;

    public SocksService(SocksRepository socksRepository) {
        this.socksRepository = socksRepository;
    }

    //todo переделать в контроллере приход с entity на dto
    //todo добавить UI с помощью vaadin

    /**
     * Check if there are sock with same color and percentage of cotton part in repository
     *  If yes - get allowed quantity and add quantity from parameter and save into repo
     *  If no - create new sock and add to repository
     *  Return sock which was saved
     * @param sock which you want to add
     * @return Sock which was added
     */
    public Sock addSocks(Sock sock){
        if(socksRepository.existsByColorAndAndCottonPart(sock.getColor(), sock.getCottonPart())){
            serviceLogger.info("зашли в if");
            Sock sockFromDB= socksRepository.getByColorAndCottonPartEquals(sock.getColor(), sock.getCottonPart());
            serviceLogger.info(String.format("socks from DB: %s, %d.", sockFromDB.getColor(), sockFromDB.getQuantity()));
            int quantity = sockFromDB.getQuantity() + sock.getQuantity();
            int id = sockFromDB.getId();
            sock.setId(id);
            sock.setQuantity(quantity);
            serviceLogger.info("sock на сохранение " + sock.getColor() + " " + sock.getQuantity());
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
     * @param sock which you want to remove
     * @return Sock which was removed
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
     *
     * Return list of all socks which suits with params (color, percentage of cotton part, operation filter)
     * If there are no socks with this filter - return empty list
     * @param color of sock
     * @param operation - lessThan, equals or moreThan
     * @param cottonPart of sock
     * @return List<Sock>
     */
    public List<Sock> getAllSocks(String color, String operation, Integer cottonPart) {
        Sock sockForRequest = checkParamsAndReturnSockIfPossible(color, operation, cottonPart);
        String assertion = operation.toLowerCase(Locale.ROOT).trim();
        serviceLogger.debug("params of URL: " + color + " " + operation + " " + cottonPart);
        if(assertion.equals(Operation.LESS_THAN.getFieldName())){
            List<Sock> result = socksRepository.getSocksByColorAndCottonPartLessThan(sockForRequest.getColor(), sockForRequest.getCottonPart());
            serviceLogger.debug("result less than: " + result );
            return result;
        } else if(assertion.equals(Operation.MORE_THAN.getFieldName())){
            List<Sock> result =socksRepository.getSocksByColorAndCottonPartGreaterThan(sockForRequest.getColor(), sockForRequest.getCottonPart());
            serviceLogger.debug("result more than: " + result );
            return result;
        } else {
            Sock result = socksRepository.getByColorAndCottonPartEquals(sockForRequest.getColor(), sockForRequest.getCottonPart());
            serviceLogger.debug("result equals: "+ result );
            return List.of(result);
        }
    }

    /**
     * Check if params are correct.
     * Throw exception if they don't
     * @param color of sock
     * @param operation lessThan, equals or moreThan
     * @param cottonPart of sock
     * @return sock if it can be created
     */
    private Sock checkParamsAndReturnSockIfPossible(String color, String operation, Integer cottonPart ){
        checkColorIfAllowed(color);
        if(Arrays.stream(Operation.values()).noneMatch(s -> s.getFieldName().equals(operation.toLowerCase().trim()))) throw new IllegalParamException("Illegal operation param");
        if(cottonPart < 0 || cottonPart > 100 ) throw new IllegalParamException("Illegal cottonPart param");
        return new Sock(color, cottonPart, 0);
    }

    /**
     * Check if color are valid and can be found in Allowed Colors Enum
     * @param color of sock
     */
    private void checkColorIfAllowed( String color){
        serviceLogger.debug("проверка цвета " + color);
        if(Arrays.stream(AllowedColors.values()).map(AllowedColors::getFieldName).noneMatch(s -> s.equals(color))) {
            throw new IllegalParamException("color is incorrect, please check color with allowed colors");
        }
    }

}
