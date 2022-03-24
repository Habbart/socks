package com.denisyan.socks_must_flow.validators.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Validator to check if token are correct
 */
@Slf4j
@Component
public class JwtValidator {


    @Value("$(jwt.secret)")
    private String jwtSecret;

    /**
     * Validate token
     *
     * @param token which you want to validate
     * @return true if OK, false if it's not OK
     */
    public boolean validateToken(String token) {
        try {
            log.info("зашли в валидацию токена. Токен:" + token);
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.warn("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.warn("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.warn("Malformed jwt");
        } catch (SignatureException sEx) {
            log.warn("Invalid signature");
        } catch (Exception e) {
            log.warn("invalid token");
        }
        return false;
    }


    /**
     * Get login from token
     *
     * @param token from which you want to take login
     * @return login of user to whom this token belongs to
     */
    public String getLoginFromToken(String token) {
        log.info("берём логин из токена");
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
