package com.denisyan.socks_must_flow.data.service;

import com.denisyan.socks_must_flow.data.entity.SamplePerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SamplePersonRepository extends JpaRepository<SamplePerson, Integer> {

}