package com.example.authservice.domain.model.gateway;

import com.example.authservice.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioGateway {
    Usuario save(Usuario usuario);
    Optional<Usuario> findByCedula(String cedula);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAll();
    void deleteByCedula(String cedula);
}
