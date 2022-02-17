package com.denisyan.socks_must_flow.validators.jwt;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Create jw token and check if token valid
 * Take secret word from properties to make jwt
 */

@Component
public class JwtProvider {

    private final Logger logger = Logger.getLogger("JWTProvider Logger");

    @Value("$(jwt.secret)")
    private String jwtSecret;

    /**
     * Generate token and return it
     * @param login of user to whom you want generate token
     * @return token
     */
    public String generateToken(String login) {
        logger.info("генерируем токен");
        Date date = Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

}
