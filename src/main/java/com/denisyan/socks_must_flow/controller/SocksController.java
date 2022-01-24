package com.denisyan.socks_must_flow.controller;


import com.denisyan.socks_must_flow.entity.Sock;
import com.denisyan.socks_must_flow.service.SocksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class SocksController {

    private final static Logger logger = LoggerFactory.getLogger("Controller Logger");

    @Autowired
    private SocksService socksService;

    /**
     * Получение списка всех носков на складе в соответствии с переданными параметрами.
     * Доступно для всех ролей
     *
     * @param color
     * @param operation
     * @param cottonPart
     * @return
     */
    @GetMapping("/api/socks")
    public List<Sock> getAllSocks(@Valid @RequestParam(value = "color") String color,
                                  @RequestParam(value = "operation") String operation,
                                  @RequestParam(value = "cottonPart") @Min(0) @Max(100) Integer cottonPart) {

        logger.info(color, operation, cottonPart);
        return socksService.getAllSocks(color, operation, cottonPart);
    }

    /**
     * Приходование носков на склад.
     * Допступно chief_warehouse
     *
     * @param sock
     */
    @PostMapping("/api/socks/income")
    public Sock addSocks(@Valid @RequestBody Sock sock) {
        return socksService.addSocks(sock);
    }

    /**
     * списание носков со склада
     * при отсутствии носков бросает исключение
     * если носков списать надо больше, то списывает только доступное значение
     * Доступно chief_warehouse
     *
     * @param sock
     */
    @PostMapping("/api/socks/outcome")
    public Sock removeSocks(@Valid @RequestBody Sock sock) {
        return socksService.removeSocks(sock);
    }


}
