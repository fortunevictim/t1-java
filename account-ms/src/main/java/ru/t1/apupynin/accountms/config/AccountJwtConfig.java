package ru.t1.apupynin.accountms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.t1.apupynin.common.security.JwtAuthenticationFilter;
import ru.t1.apupynin.common.security.JwtConfig;
import ru.t1.apupynin.common.security.JwtUtil;

@Configuration
@Import(ru.t1.apupynin.common.security.JwtConfig.class)
public class AccountJwtConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
        return new JwtAuthenticationFilter(jwtUtil);
    }
}

