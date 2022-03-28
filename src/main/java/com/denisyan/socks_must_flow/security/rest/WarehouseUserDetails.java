package com.denisyan.socks_must_flow.security.rest;


import com.denisyan.socks_must_flow.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * realization of standard Spring Security UserDetails interface
 * You can check Spring Security documentation for more information
 */

public class WarehouseUserDetails implements UserDetails {

    private String login;

    private String password;

    private boolean isBlocked;

    private Collection<? extends GrantedAuthority> grantedAuthorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public static UserDetails fromEntityToWarehouseUserDetails(User user) {
        WarehouseUserDetails warehouseUserDetails = new WarehouseUserDetails();
        warehouseUserDetails.login = user.getLogin();
        warehouseUserDetails.password = user.getPassword();
        warehouseUserDetails.isBlocked = user.isBlocked();
        warehouseUserDetails.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()));
        return warehouseUserDetails;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isBlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
