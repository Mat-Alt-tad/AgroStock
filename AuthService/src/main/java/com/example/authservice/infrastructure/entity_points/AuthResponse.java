package com.example.authservice.infrastructure.entity_points;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String cedula;
    private String nombre;
    private String rol;
}
