package ru.t1.apupynin.clientms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.t1.apupynin.common.security.JwtAuthenticationFilter;
import ru.t1.apupynin.clientms.security.BlockedClientFilter;
import ru.t1.apupynin.clientms.security.RolesEnrichmentFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final BlockedClientFilter blockedClientFilter;
    private final RolesEnrichmentFilter rolesEnrichmentFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, BlockedClientFilter blockedClientFilter, RolesEnrichmentFilter rolesEnrichmentFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.blockedClientFilter = blockedClientFilter;
        this.rolesEnrichmentFilter = rolesEnrichmentFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/clients/register").anonymous()
                        .requestMatchers(HttpMethod.POST, "/api/clients/login").anonymous()
                        .requestMatchers("/actuator/**", "/h2-console/**").anonymous()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rolesEnrichmentFilter, JwtAuthenticationFilter.class)
                .addFilterAfter(blockedClientFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}


