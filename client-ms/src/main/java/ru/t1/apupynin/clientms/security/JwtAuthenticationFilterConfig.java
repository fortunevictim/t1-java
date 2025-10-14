package ru.t1.apupynin.clientms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.t1.apupynin.common.security.JwtAuthenticationFilter;

@Configuration
public class JwtAuthenticationFilterConfig {

    @Bean
    @Primary
    public ru.t1.apupynin.common.security.JwtAuthenticationFilter jwtAuthenticationFilter(ru.t1.apupynin.common.security.JwtUtil jwtUtil) {
        return new ru.t1.apupynin.common.security.JwtAuthenticationFilter(jwtUtil);
    }
}
