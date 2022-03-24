package com.denisyan.socks_must_flow.dao;

import com.denisyan.socks_must_flow.entity.Sock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repo for socks
 */
public interface SocksRepository extends JpaRepository<Sock, Integer> {

    boolean existsByColorAndAndCottonPart(String color, int cottonPart);

    Sock getByColorAndCottonPartEquals(String color, int cottonPart);

    List<Sock> getSocksByColorAndCottonPartGreaterThan(String color, int cottonPart);

    List<Sock> getSocksByColorAndCottonPartLessThan(String color, int cottonPart);


}
