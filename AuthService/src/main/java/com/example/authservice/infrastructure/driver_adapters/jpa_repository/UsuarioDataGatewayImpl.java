package com.example.authservice.infrastructure.driver_adapters.jpa_repository;

import lombok.RequiredArgsConstructor;
import com.example.authservice.domain.model.Usuario;
import com.example.authservice.domain.model.gateway.UsuarioGateway;
import com.example.authservice.infrastructure.mapper.UsuarioMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UsuarioDataGatewayImpl implements UsuarioGateway {

    private final UsuarioDataJpaRepository repository;
    private final UsuarioMapper mapper;

    @Override
    public Usuario save(Usuario usuario) {
        return mapper.toDomain(repository.save(mapper.toData(usuario)));
    }

    @Override
    public Optional<Usuario> findByCedula(String cedula) {
        return repository.findById(cedula).map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByCedula(String cedula) {
        repository.deleteById(cedula);
    }
}
