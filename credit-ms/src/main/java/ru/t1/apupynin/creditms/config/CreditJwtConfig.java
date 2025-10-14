package ru.t1.apupynin.creditms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.t1.apupynin.common.security.JwtConfig;

@Configuration
@Import(ru.t1.apupynin.common.security.JwtConfig.class)
public class CreditJwtConfig {
}

