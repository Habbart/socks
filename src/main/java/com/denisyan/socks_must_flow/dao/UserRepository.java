package com.denisyan.socks_must_flow.dao;

import com.denisyan.socks_must_flow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLogin(String login);
}