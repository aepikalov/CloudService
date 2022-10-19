package ru.netology.cloudservice.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.cloudservice.service.AuthorizationService;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/logout")
@Validated
public class LogoutController {
    private final AuthorizationService authorizationService;

    public LogoutController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public void logout(@RequestHeader("auth-token") @NotBlank String authToken) {
        authorizationService.logout(authToken);
    }
}