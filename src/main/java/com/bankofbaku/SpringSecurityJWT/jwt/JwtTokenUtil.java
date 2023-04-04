package com.bankofbaku.SpringSecurityJWT.jwt;

import com.bankofbaku.SpringSecurityJWT.entities.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final long EXPIRATION_DURATION = 24 * 60 * 60 *1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT expired", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOGGER.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            LOGGER.error("Signature validation failed");
        }

        return false;
    }
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;
    public String generateAccessToken(User user){
        return Jwts.builder()
                .setSubject(user.getId()+","+ user.getUsername())
                .claim("roles", user.getRoles().toString())
                .setIssuer("Nurlana")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_DURATION))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();

    }
    public String getSubject(String token){
        return parseClaims(token).getSubject();
    }
    public Claims parseClaims(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
