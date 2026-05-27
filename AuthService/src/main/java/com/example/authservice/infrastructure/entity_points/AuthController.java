package com.example.authservice.infrastructure.entity_points;

import lombok.RequiredArgsConstructor;
import com.example.authservice.domain.model.Usuario;
import com.example.authservice.domain.useCase.UsuarioUseCase;
import com.example.authservice.infrastructure.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioUseCase usuarioUseCase;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> register(@RequestBody UsuarioRequest request) {
        Usuario saved = usuarioUseCase.save(toDomain(request));
        return ResponseEntity.ok(toResponse(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioUseCase.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(usuario.getEmail(), usuario.getRol());

        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .cedula(usuario.getCedula())
                .nombre(usuario.getNombre())
                .rol(usuario.getRol())
                .build());
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> findAll() {
        List<UsuarioResponse> lista = usuarioUseCase.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuarios/{cedula}")
    public ResponseEntity<UsuarioResponse> findByCedula(@PathVariable String cedula) {
        return usuarioUseCase.findByCedula(cedula)
                .map(u -> ResponseEntity.ok(toResponse(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/usuarios/{cedula}")
    public ResponseEntity<Void> delete(@PathVariable String cedula) {
        usuarioUseCase.deleteByCedula(cedula);
        return ResponseEntity.noContent().build();
    }

    private Usuario toDomain(UsuarioRequest req) {
        return Usuario.builder()
                .cedula(req.getCedula())
                .nombre(req.getNombre())
                .email(req.getEmail())
                .password(req.getPassword())
                .rol(req.getRol())
                .build();
    }

    private UsuarioResponse toResponse(Usuario u) {
        return UsuarioResponse.builder()
                .cedula(u.getCedula())
                .nombre(u.getNombre())
                .email(u.getEmail())
                .rol(u.getRol())
                .build();
    }
}
