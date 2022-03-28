package com.denisyan.socks_must_flow.controller;


import com.denisyan.socks_must_flow.dto.SockDto;
import com.denisyan.socks_must_flow.entity.Sock;
import com.denisyan.socks_must_flow.service.SocksRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Controller for add, remove and get stock of socks which available on warehouse
 */

@RequiredArgsConstructor
@Slf4j
@RestController
public class SocksController {


    private final SocksRestService socksService;
    private final ModelMapper modelMapper;

    /**
     * Get list of all Socks from warehouse stock according to parameters.
     * Allowed to all roles
     * sample of request:
     * /api/socks?color=red&operation=moreThan&cottonPart=90 — should return red socks with cotton part more than 90%
     *
     * @param color      - allowed color
     * @param operation  - moreThan, lessThan, equals
     * @param cottonPart - 0...100 cotton part in socks
     * @return list of socks which suits with params
     */
    @GetMapping("/api/socks")
    public List<Sock> getAllSocks(@Valid @RequestParam(value = "color") String color,
                                  @RequestParam(value = "operation") String operation,
                                  @RequestParam(value = "cottonPart") @Min(0) @Max(100) Integer cottonPart) {

        log.info(color, operation, cottonPart);
        return socksService.RestGetAllSocksByColorAndOperation(color, operation, cottonPart);
    }

    /**
     * Add socks to warehouse stock
     * Allowed for Chief of warehouse
     *
     * @param sockDto which you want to add
     * @return sockDto which was added
     */
    @PostMapping("/api/socks/income")
    public SockDto addSocks(@RequestBody SockDto sockDto) {
        Sock sock = modelMapper.map(sockDto, Sock.class);
        Sock sockFromService = socksService.addSocks(sock);

        return modelMapper.map(sockFromService, SockDto.class);
    }

    /**
     * Remove socks from warehouse
     * If socks can't be found - throw the exception
     * If you want to remove more socks than warehouse has - remove only possible quantity of socks, so this kind of socks became 0
     * Allowed only for Chief of Warehouse
     *
     * @param sockDto which you want to remove
     * @return sockDto which was removed
     */
    @PostMapping("/api/socks/outcome")
    public SockDto removeSocks(@Valid @RequestBody SockDto sockDto) {
        Sock sock = modelMapper.map(sockDto, Sock.class);
        Sock sockFromService = socksService.RestRemoveSocks(sock);

        return modelMapper.map(sockFromService, SockDto.class);
    }


}
