package com.example.authservice.application.config;

import com.example.authservice.domain.model.gateway.EncrypterGateway;
import com.example.authservice.domain.model.gateway.UsuarioGateway;
import com.example.authservice.domain.useCase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public UsuarioUseCase usuarioUseCase(UsuarioGateway usuarioGateway,
                                          EncrypterGateway encrypterGateway) {
        return new UsuarioUseCase(usuarioGateway, encrypterGateway);
    }
}
