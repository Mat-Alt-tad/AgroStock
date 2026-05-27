package com.example.authservice.infrastructure.entity_points;

import lombok.Data;

@Data
public class UsuarioRequest {
    private String cedula;
    private String nombre;
    private String email;
    private String password;
    private String rol;
}
