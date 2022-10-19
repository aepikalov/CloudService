package ru.netology.cloudservice.exceptions;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("User is not authorized");
    }
}