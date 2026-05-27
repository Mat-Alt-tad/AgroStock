package com.example.authservice.infrastructure.entity_points;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
