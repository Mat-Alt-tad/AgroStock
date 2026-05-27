package com.example.authservice.infrastructure.entity_points;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponse {
    private String cedula;
    private String nombre;
    private String email;
    private String rol;
}
