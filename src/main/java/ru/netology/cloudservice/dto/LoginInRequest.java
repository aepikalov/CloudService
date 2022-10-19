package ru.netology.cloudservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Validated
public class LoginInRequest {
    @NotBlank
    private final String login;
    @NotBlank
    private final String password;
}