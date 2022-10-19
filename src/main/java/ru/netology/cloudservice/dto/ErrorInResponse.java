package ru.netology.cloudservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorInResponse {
    private final String message;
    private final int id;
}