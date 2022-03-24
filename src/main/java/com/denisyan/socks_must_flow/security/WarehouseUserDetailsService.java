package com.denisyan.socks_must_flow.security;


import com.denisyan.socks_must_flow.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Realization of standard Spring Security UserDetailService interface
 * You can check Spring Security documentation for more information
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("берём юзера по Логину");
        return WarehouseUserDetails.fromEntityToWarehouseUserDetails(userService.findByLogin(userName));
    }
}
