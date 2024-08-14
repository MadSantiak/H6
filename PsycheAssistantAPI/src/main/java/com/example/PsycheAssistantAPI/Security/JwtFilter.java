package com.example.PsycheAssistantAPI.Security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String username = jwtUtil.extractEmail(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (jwtUtil.validateToken(token, username)) {
                        jwtUtil.setAuthentication(username, httpRequest);
                    }
                }
            } catch (ExpiredJwtException e) {
                // Handle token expiration and attempt refresh
                String refreshToken = httpRequest.getHeader("Refresh-Token");
                if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken, e.getClaims().getSubject())) {
                    String newToken = jwtUtil.generateToken(e.getClaims().getSubject());
                    httpResponse.setHeader("Authorization", "Bearer " + newToken);
                    jwtUtil.setAuthentication(e.getClaims().getSubject(), httpRequest);
                } else {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired and refresh token is invalid or missing.");
                    return;
                }
            } catch (Exception e) {
                // Handle other exceptions
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token.");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
