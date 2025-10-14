package ru.t1.apupynin.clientms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.t1.apupynin.clientms.entity.Client;
import ru.t1.apupynin.clientms.entity.User;
import ru.t1.apupynin.clientms.entity.UserRole;
import ru.t1.apupynin.clientms.enums.Role;
import ru.t1.apupynin.clientms.repository.ClientRepository;
import ru.t1.apupynin.clientms.repository.UserRepository;
import ru.t1.apupynin.clientms.repository.UserRoleRepository;
import ru.t1.apupynin.clientms.service.BlacklistService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class RolesEnrichmentFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ClientRepository clientRepository;
    private final BlacklistService blacklistService;

    public RolesEnrichmentFilter(UserRepository userRepository,
                                 UserRoleRepository userRoleRepository,
                                 ClientRepository clientRepository,
                                 BlacklistService blacklistService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.clientRepository = clientRepository;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var ctx = SecurityContextHolder.getContext();
        if (ctx.getAuthentication() != null && ctx.getAuthentication().getPrincipal() instanceof String principal) {
            User user = userRepository.findByLogin(principal).orElse(null);
            if (user != null) {
                List<UserRole> roles = userRoleRepository.findByUserId(user.getId());
                boolean blocked = false;
                Client client = clientRepository.findAll().stream().filter(c -> Objects.equals(c.getUserId(), user.getId())).findFirst().orElse(null);
                if (client != null) {
                    blocked = blacklistService.isInBlacklist(client.getDocumentType(), client.getDocumentId());
                }
                List<GrantedAuthority> authorities;
                if (blocked) {
                    authorities = List.of(new SimpleGrantedAuthority("ROLE_" + Role.BLOCKED_CLIENT.name()));
                } else {
                    authorities = roles.stream()
                            .map(UserRole::getRole)
                            .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                            .collect(Collectors.toList());
                }
                var newAuth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                ctx.setAuthentication(newAuth);
            }
        }
        filterChain.doFilter(request, response);
    }
}



