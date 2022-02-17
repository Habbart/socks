package com.denisyan.socks_must_flow.validators.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;


/**
 * Validator to check if token are correct
 */

@Component
public class JwtValidator {

    private final Logger logger = Logger.getLogger("JWTValidator Logger");

    @Value("$(jwt.secret)")
    private String jwtSecret;

    /**
     * Validate token
     * @param token which you want to validate
     * @return true if OK, false if it's not OK
     */
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



    /**
     * Get login from token
     * @param token from which you want to take login
     * @return login of user to whom this token belongs to
     */
    public String getLoginFromToken(String token){
        logger.info("берём логин из токена");
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

}
