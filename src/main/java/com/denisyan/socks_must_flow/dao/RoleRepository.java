package com.denisyan.socks_must_flow.dao;

import com.denisyan.socks_must_flow.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Roles
 */

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);
}