package ru.t1.apupynin.clientms.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.apupynin.clientms.entity.User;
import ru.t1.apupynin.clientms.entity.UserRole;
import ru.t1.apupynin.clientms.enums.Role;
import ru.t1.apupynin.clientms.repository.UserRoleRepository;
import ru.t1.apupynin.common.security.JwtUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    @Mock JwtUtil jwtUtil;
    @Mock UserRoleRepository userRoleRepository;

    @InjectMocks JwtTokenService jwtTokenService;

    @Test
    void generateToken_addsRolesClaim() {
        User user = new User("u1", "p", "e@example.com");
        user.setId(5L);
        when(userRoleRepository.findByUserId(5L)).thenReturn(List.of(
                new UserRole(5L, Role.MASTER), new UserRole(5L, Role.CURRENT_CLIENT)
        ));
        when(jwtUtil.generateToken(eq("u1"), anyList(), anyMap())).thenReturn("token");

        String token = jwtTokenService.generateToken(user);
        assertEquals("token", token);
        verify(jwtUtil).generateToken(eq("u1"), argThat(list -> list.contains("MASTER") && list.contains("CURRENT_CLIENT")), anyMap());
    }
}
