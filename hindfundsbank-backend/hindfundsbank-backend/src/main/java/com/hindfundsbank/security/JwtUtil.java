package com.hindfundsbank.security;

import com.hindfundsbank.config.JwtProperties;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility for generating and validating JSON Web Tokens (JWT).
 * Reads secret and expiration values from JwtProperties bean
 * (configured via @ConfigurationProperties(prefix = "jwt")).
 */
@Component
public class JwtUtil {

    private final JwtProperties props;

    public JwtUtil(JwtProperties props) {
        this.props = props;
    }

    /**
     * Generate a JWT containing the subject (username/email) set as the token subject,
     * with issued at and expiration claims.
     *
     * @param subject the username or identifier for whom the token is issued
     * @return a signed JWT string
     */
    public String generateToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + props.getExpiration());

        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, props.getSecret())
            .compact();
    }

    /**
     * Extract the subject (username/email) from the given JWT.
     *
     * @param token the JWT string
     * @return the subject claim
     * @throws JwtException if the token is invalid or cannot be parsed
     */
    public String getUsername(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(props.getSecret())
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    /**
     * Validate the given JWT for correctness of signature and expiration.
     *
     * @param token the JWT string
     * @return true if valid; false otherwise
     */
    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(props.getSecret()).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException |
                 MalformedJwtException | SignatureException |
                 IllegalArgumentException ex) {
            // You might want to log the exception or rethrow a custom exception here
            return false;
        }
    }
}
