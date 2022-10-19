package ru.netology.cloudservice.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LoginInResponse {
    private String authToken;

    @JsonGetter("auth-token")
    public String getAuthToken() {
        return authToken;
    }
}