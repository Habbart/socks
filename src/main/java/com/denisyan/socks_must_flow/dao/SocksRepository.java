package com.denisyan.socks_must_flow.dao;

import com.denisyan.socks_must_flow.entity.Sock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;


public interface SocksRepository extends JpaRepository<Sock, Integer> {

    @Query("select (count(s) > 0) from Sock s where s.color = ?1 and s.cottonPart = ?2")
    boolean existsByColorAndAndCottonPart(String color, @Min(value = 0) @Max(value = 100)  int cottonPart);
    @Query("select s from Sock s where s.color = ?1 and s.cottonPart = ?2")
    Sock getByColorAndCottonPartEquals(String color, @Min(value = 0) @Max(value = 100)  int cottonPart);
    List<Sock> getSocksByColorAndCottonPartGreaterThan(String color, @Min(value = 0) @Max(value = 100)  int cottonPart);
    List<Sock> getSocksByColorAndCottonPartLessThan(String color, @Min(value = 0) @Max(value = 100) int cottonPart);


}
