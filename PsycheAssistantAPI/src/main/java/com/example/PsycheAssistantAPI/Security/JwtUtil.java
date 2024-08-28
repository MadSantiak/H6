package com.example.PsycheAssistantAPI.Security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    /**
     * Fetch relevant keys/configurations from application.properties (actually imported from credentials.properties)
     */
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long JWT_EXPIRATION;

    @Value("${jwt.refresh-token-expiration}")
    private long REFRESH_TOKEN_EXPIRATION;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Generates a new token.
     * @param email
     * @return
     */
    public String generateToken(String email) {
        try {
            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();
        }
        catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * Extracts the email (subject) of the token.
     * @param token
     * @return
     */
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates the token supplied matches the passed email.
     * @param token
     * @param email
     * @return
     */
    public boolean validateToken(String token, String email) {
        String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email));
    }

    /**
     * Checks if token is expired.
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Generates refresh-token.
     * @param email
     * @return
     */
    public String generateRefreshToken(String email) {
        try {
            return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();
        } catch (Exception e) {
            return e.toString();
        }
    }

    /**
     * Validates refresh-token based on passed email.
     * @param refreshToken
     * @param email
     * @return
     */
    public boolean validateRefreshToken(String refreshToken, String email) {
        try {
            String tokenEmail = extractEmail(refreshToken);
            return tokenEmail.equals(email) && !isTokenExpired(refreshToken);
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Sets authentication for the username (email).
     * @param username
     * @param request
     */
    public void setAuthentication(String username, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
