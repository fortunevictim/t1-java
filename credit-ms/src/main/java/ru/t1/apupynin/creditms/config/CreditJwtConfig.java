package ru.t1.apupynin.creditms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.t1.apupynin.common.security.JwtAuthenticationFilter;
import ru.t1.apupynin.common.security.JwtConfig;
import ru.t1.apupynin.common.security.JwtUtil;


@Configuration
@Import(ru.t1.apupynin.common.security.JwtConfig.class)
public class CreditJwtConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
        return new JwtAuthenticationFilter(jwtUtil);
    }
}

