package com.example.PsycheAssistantAPI.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF protection
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                //.requestMatchers("/api/auth/**").permitAll()  // Allow access to auth endpoints
                                .requestMatchers("/**").permitAll()  // Allow access to ALL endpoints (for development)
                                .requestMatchers("/console/h2").permitAll()
                                .anyRequest().authenticated()  // Require authentication for all other requests
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin())
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);  // Add JWT filter

        return http.build();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtil);
    }
}