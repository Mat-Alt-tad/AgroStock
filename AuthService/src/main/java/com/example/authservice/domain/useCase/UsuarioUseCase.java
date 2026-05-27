package com.example.authservice.domain.useCase;

import lombok.RequiredArgsConstructor;
import com.example.authservice.domain.model.Usuario;
import com.example.authservice.domain.model.gateway.EncrypterGateway;
import com.example.authservice.domain.model.gateway.UsuarioGateway;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;
    private final EncrypterGateway encrypterGateway;

    public Usuario save(Usuario usuario) {
        try {
            usuario.setPassword(encrypterGateway.encode(usuario.getPassword()));
            return usuarioGateway.save(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar Usuario: " + e.getMessage());
        }
    }

    public Optional<Usuario> findByCedula(String cedula) {
        try {
            return usuarioGateway.findByCedula(cedula);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar Usuario: " + e.getMessage());
        }
    }

    public Optional<Usuario> findByEmail(String email) {
        try {
            return usuarioGateway.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar Usuario por email: " + e.getMessage());
        }
    }

    public List<Usuario> findAll() {
        try {
            return usuarioGateway.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar Usuarios: " + e.getMessage());
        }
    }

    public void deleteByCedula(String cedula) {
        try {
            usuarioGateway.deleteByCedula(cedula);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar Usuario: " + e.getMessage());
        }
    }
}
