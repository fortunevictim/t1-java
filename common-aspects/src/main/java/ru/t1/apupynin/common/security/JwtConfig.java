package ru.t1.apupynin.common.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.ttl-ms}")
    private long ttlMs;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secret, ttlMs);
    }
}
