package ru.t1.apupynin.clientms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class BlockedClientFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            boolean isBlocked = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .anyMatch(a -> a.equals("ROLE_BLOCKED_CLIENT"));
            if (isBlocked) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Client is blocked");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}



