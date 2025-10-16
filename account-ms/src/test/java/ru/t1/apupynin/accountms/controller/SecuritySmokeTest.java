package ru.t1.apupynin.accountms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.main.allow-bean-definition-overriding=true",
        "security.jwt.secret=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==",
        "security.jwt.ttl-ms=3600000"
})
@AutoConfigureMockMvc
class SecuritySmokeTest {

    @Autowired MockMvc mockMvc;

    @Test
    void protectedEndpoints_requireAuth() throws Exception {
        mockMvc.perform(get("/api/anything"))
                .andExpect(status().isForbidden());
    }
}
