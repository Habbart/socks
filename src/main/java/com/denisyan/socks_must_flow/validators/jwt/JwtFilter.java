package com.denisyan.socks_must_flow.validators.jwt;

import com.denisyan.socks_must_flow.security.rest.WarehouseUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;


/**
 * Extends standard Generic Filter Bean from Spring Security framework
 * For more information check Spring Security documentation
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {


    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    private final JwtValidator validator;
    private final WarehouseUserDetailsService warehouseUserDetailsService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        log.debug("servletRequest: " + servletRequest);
        if (token != null && validator.validateToken(token)) {
            log.debug("зашли в фильтр токена");
            String userLogin = validator.getLoginFromToken(token);
            UserDetails warehouseUserDetails = warehouseUserDetailsService.loadUserByUsername(userLogin);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(warehouseUserDetails, null, warehouseUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("token: " + token + "username: " + userLogin);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith(BEARER)) { // После извлечения токена мне нужно проверить чтобы он начинался со слова Bearer (носитель с англ.).
            return bearer.substring(7);   //Почему именно с Bearer? Такой стандарт RFC6750 для токенов с которым более подробно можно ознакомиться по ссылке https://datatracker.ietf.org/doc/html/rfc6750.
        }
        return bearer;
    }
}
