package ru.t1.apupynin.clientms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.t1.apupynin.clientms.dto.LoginRequest;
import ru.t1.apupynin.clientms.dto.LoginResponse;
import ru.t1.apupynin.clientms.dto.RegistrationRequest;
import ru.t1.apupynin.clientms.dto.UserResponse;
import ru.t1.apupynin.clientms.entity.User;
import ru.t1.apupynin.clientms.repository.UserRepository;
import ru.t1.apupynin.clientms.service.JwtTokenService;
import ru.t1.apupynin.clientms.service.RegistrationService;
import ru.t1.apupynin.common.aspects.annotation.HttpIncomeRequestLog;
import ru.t1.apupynin.common.aspects.annotation.HttpOutcomeRequestLog;

import java.util.Map;

@RestController
@RequestMapping("/api/clients")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;

    public RegistrationController(RegistrationService registrationService,
                                AuthenticationManager authenticationManager,
                                UserRepository userRepository,
                                JwtTokenService jwtTokenService) {
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/register")
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    public ResponseEntity<UserResponse> register(@RequestBody RegistrationRequest request) {
        User user = registrationService.register(request);
        UserResponse resp = new UserResponse();
        resp.id = user.getId();
        resp.login = user.getLogin();
        resp.email = user.getEmail();
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.password())
        );
        
        User user = userRepository.findByLogin(request.login())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String token = jwtTokenService.generateToken(user);
        
        return ResponseEntity.ok(new LoginResponse(token, user.getLogin(), user.getEmail()));
    }

}



