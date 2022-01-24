package com.denisyan.socks_must_flow.security;


import com.denisyan.socks_must_flow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class WarehouseUserDetailsService implements UserDetailsService {

    private final Logger logger = Logger.getLogger("WarehouseUserDetailsService Logger");

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        logger.info("берём юзера по Логину");
        return WarehouseUserDetails.fromEntityToWarehouseUserDetails(userService.findByLogin(userName));
    }
}
