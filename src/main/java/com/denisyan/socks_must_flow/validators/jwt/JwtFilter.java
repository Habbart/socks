package com.denisyan.socks_must_flow.validators.jwt;

import com.denisyan.socks_must_flow.security.WarehouseUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.logging.Logger;

import static org.springframework.util.StringUtils.hasText;


@Component
public class JwtFilter extends GenericFilterBean {

    private final Logger logger = Logger.getLogger("JwtFilter Logger");

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private WarehouseUserDetailsService warehouseUserDetailsService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("do filter...");
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);
        logger.info("servletRequest: " + servletRequest);
        if (token != null && jwtProvider.validateToken(token)) {
            logger.info("зашли в фильтр токена");
            String userLogin = jwtProvider.getLoginFromToken(token);
            UserDetails warehouseUserDetails = warehouseUserDetailsService.loadUserByUsername(userLogin);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(warehouseUserDetails, null, warehouseUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            logger.info("token: " + token + "username: " + userLogin);
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
