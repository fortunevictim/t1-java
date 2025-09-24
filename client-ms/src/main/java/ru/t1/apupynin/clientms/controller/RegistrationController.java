package ru.t1.apupynin.clientms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.apupynin.clientms.dto.RegistrationRequest;
import ru.t1.apupynin.clientms.dto.UserResponse;
import ru.t1.apupynin.clientms.entity.User;
import ru.t1.apupynin.clientms.service.RegistrationService;

@RestController
@RequestMapping("/api/clients")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegistrationRequest request) {
        User user = registrationService.register(request);
        UserResponse resp = new UserResponse();
        resp.id = user.getId();
        resp.login = user.getLogin();
        resp.email = user.getEmail();
        return ResponseEntity.ok(resp);
    }
}



