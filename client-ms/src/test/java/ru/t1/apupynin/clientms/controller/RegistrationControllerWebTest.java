package ru.t1.apupynin.clientms.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.t1.apupynin.clientms.dto.LoginRequest;
import ru.t1.apupynin.clientms.dto.RegistrationRequest;
import ru.t1.apupynin.clientms.entity.User;
import ru.t1.apupynin.clientms.repository.UserRepository;
import ru.t1.apupynin.clientms.service.JwtTokenService;
import ru.t1.apupynin.clientms.service.RegistrationService;
import ru.t1.apupynin.common.security.JwtAuthenticationFilter;
import ru.t1.apupynin.clientms.security.BlockedClientFilter;
import ru.t1.apupynin.clientms.security.RolesEnrichmentFilter;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RegistrationController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class RegistrationControllerWebTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RegistrationService registrationService;
    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    UserRepository userRepository;
    @MockBean
    JwtTokenService jwtTokenService;

    @MockBean JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean BlockedClientFilter blockedClientFilter;
    @MockBean RolesEnrichmentFilter rolesEnrichmentFilter;

    @Test
    void register_returns200_andUserPayload() throws Exception {
        User user = new User("login1", "pwd", "e@e.com");
        user.setId(100L);
        when(registrationService.register(any(RegistrationRequest.class))).thenReturn(user);

        String body = "{\n" +
                "  \"clientId\": \"c1\",\n" +
                "  \"login\": \"login1\",\n" +
                "  \"password\": \"pwd\",\n" +
                "  \"email\": \"e@e.com\"\n" +
                "}";

        mockMvc.perform(post("/api/clients/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.login").value("login1"))
                .andExpect(jsonPath("$.email").value("e@e.com"));
    }

    @Test
    void login_returnsToken() throws Exception {
        Authentication auth = new UsernamePasswordAuthenticationToken("login1", null);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        User user = new User("login1", "pwd", "e@e.com");
        user.setId(5L);
        when(userRepository.findByLogin("login1")).thenReturn(java.util.Optional.of(user));
        when(jwtTokenService.generateToken(user)).thenReturn("jwt-token");

        String body = "{\n" +
                "  \"login\": \"login1\",\n" +
                "  \"password\": \"pwd\"\n" +
                "}";

        mockMvc.perform(post("/api/clients/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.login").value("login1"));
    }
}
