package com.example.authservice.domain.model.gateway;

public interface EncrypterGateway {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
