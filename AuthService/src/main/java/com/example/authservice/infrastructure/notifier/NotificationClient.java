package com.example.authservice.infrastructure.notifier;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationClient {

    private final RestTemplate restTemplate;

    @Value("${services.notification-url}")
    private String notificationUrl;

    public void enviarBienvenida(String nombre, String email, String cedula) {
        Map<String, Object> body = Map.of(
            "tipo", "EXITO",
            "titulo", "Bienvenido a AgroStock",
            "email", email,
            "mensaje", "Bienvenido " + nombre + " a AgroStock. Tu cuenta ha sido creada exitosamente.",
            "destinatarioId", cedula
        );
        try {
            restTemplate.postForEntity(notificationUrl + "/api/notificaciones", body, Void.class);
        } catch (Exception e) {
            System.err.println("Error al enviar notificacion de bienvenida: " + e.getMessage());
        }
    }
}
