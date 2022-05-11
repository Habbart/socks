package com.denisyan.socks_must_flow.repositories;

import com.denisyan.socks_must_flow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * repo for users
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLogin(String login);
}