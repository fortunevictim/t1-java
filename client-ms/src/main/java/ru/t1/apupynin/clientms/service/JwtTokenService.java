package ru.t1.apupynin.clientms.service;

import org.springframework.stereotype.Service;
import ru.t1.apupynin.clientms.entity.User;
import ru.t1.apupynin.clientms.entity.UserRole;
import ru.t1.apupynin.clientms.enums.Role;
import ru.t1.apupynin.clientms.repository.UserRoleRepository;
import ru.t1.apupynin.common.security.JwtUtil;

import java.util.List;
import java.util.Map;

@Service
public class JwtTokenService {

    private final JwtUtil jwtUtil;
    private final UserRoleRepository userRoleRepository;

    public JwtTokenService(JwtUtil jwtUtil, UserRoleRepository userRoleRepository) {
        this.jwtUtil = jwtUtil;
        this.userRoleRepository = userRoleRepository;
    }

    public String generateToken(User user) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        List<String> roles = userRoles.stream()
                .map(UserRole::getRole)
                .map(Role::name)
                .toList();
        
        Map<String, Object> extraClaims = Map.of(
                "userId", user.getId(),
                "email", user.getEmail()
        );
        
        return jwtUtil.generateToken(user.getLogin(), roles, extraClaims);
    }

    public String generateToken(User user, Map<String, Object> extraClaims) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        List<String> roles = userRoles.stream()
                .map(UserRole::getRole)
                .map(Role::name)
                .toList();
        
        Map<String, Object> allClaims = Map.of(
                "userId", user.getId(),
                "email", user.getEmail()
        );
        
        if (extraClaims != null) {
            allClaims.putAll(extraClaims);
        }
        
        return jwtUtil.generateToken(user.getLogin(), roles, allClaims);
    }
}
