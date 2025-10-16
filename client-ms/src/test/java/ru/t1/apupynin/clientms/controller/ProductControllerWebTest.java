package ru.t1.apupynin.clientms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.t1.apupynin.clientms.mapper.DtoMapper;
import ru.t1.apupynin.clientms.repository.ProductRepository;
import ru.t1.apupynin.common.security.JwtAuthenticationFilter;
import ru.t1.apupynin.clientms.security.BlockedClientFilter;
import ru.t1.apupynin.clientms.security.RolesEnrichmentFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = ProductController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerWebTest {

    @Autowired MockMvc mockMvc;

    @MockBean ProductRepository productRepository;
    @MockBean DtoMapper mapper;

    @MockBean JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean BlockedClientFilter blockedClientFilter;
    @MockBean RolesEnrichmentFilter rolesEnrichmentFilter;

    @Test
    void create_returns201() throws Exception {
        String body = "{\n  \"name\": \"Prod\", \"key\": \"DC\", \"productId\": \"p1\"\n}";
        when(productRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}
