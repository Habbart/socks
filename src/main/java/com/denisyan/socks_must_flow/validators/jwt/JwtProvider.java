package com.denisyan.socks_must_flow.validators.jwt;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtProvider {

    private final Logger logger = Logger.getLogger("JWTProvider Logger");

    @Value("$(jwt.secret)")
    private String jwtSecret;


    public String generateToken(String login) {
        logger.info("генерируем токен");
        Date date = Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            logger.info("зашли в валидацию токена. Токен:" + token);
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            logger.severe("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            logger.severe("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            logger.severe("Malformed jwt");
        } catch (SignatureException sEx) {
            logger.severe("Invalid signature");
        } catch (Exception e) {
            logger.severe("invalid token");
        }
        return false;
    }

    public String getLoginFromToken(String token){
        logger.info("берём логин из токена");
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
