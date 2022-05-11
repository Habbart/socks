package com.denisyan.socks_must_flow.repositories;

import com.denisyan.socks_must_flow.entity.Sock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Repo for socks
 */
public interface SocksRepository extends JpaRepository<Sock, Long> {

    boolean existsByColorAndAndCottonPart(String color, int cottonPart);

    Sock getByColorAndCottonPartEquals(String color, int cottonPart);

    List<Sock> getSocksByColorAndCottonPartGreaterThan(String color, int cottonPart);

    List<Sock> getSocksByColorAndCottonPartLessThan(String color, int cottonPart);

    @Query("select s from Sock s where s.color = ?1")
    List<Sock> findByColor(String color);

    default Map<String, Integer> findAllColorsAndQuantities(){
        return findAll().stream().collect(Collectors.toMap(Sock::getColor, Sock::getQuantity, Integer::sum));
    }

}
