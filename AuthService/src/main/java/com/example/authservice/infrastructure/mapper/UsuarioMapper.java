package com.example.authservice.infrastructure.mapper;

import com.example.authservice.domain.model.Usuario;
import com.example.authservice.infrastructure.driver_adapters.jpa_repository.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toDomain(UsuarioData data) {
        if (data == null) return null;
        return Usuario.builder()
                .cedula(data.getCedula())
                .nombre(data.getNombre())
                .email(data.getEmail())
                .password(data.getPassword())
                .rol(data.getRol())
                .build();
    }

    public UsuarioData toData(Usuario domain) {
        if (domain == null) return null;
        return UsuarioData.builder()
                .cedula(domain.getCedula())
                .nombre(domain.getNombre())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .rol(domain.getRol())
                .build();
    }
}
