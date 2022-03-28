package com.denisyan.socks_must_flow.service;

import com.denisyan.socks_must_flow.dao.SocksRepository;
import com.denisyan.socks_must_flow.entity.Sock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SocksViewService {

    //todo сделать в методе FindAll ограниченный вывод сущностей

    private final transient SocksRepository socksRepository;

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

    public Optional<Sock> findById(Long id){
        return socksRepository.findById(id);
    }
}
