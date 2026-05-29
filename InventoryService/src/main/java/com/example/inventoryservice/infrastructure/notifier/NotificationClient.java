package com.example.inventoryservice.infrastructure.notifier;

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

    public void enviarNotificacion(String tipo, String titulo, String email, String mensaje, String destinatarioId) {
        Map<String, Object> body = Map.of(
            "tipo", tipo,
            "titulo", titulo,
            "email", email,
            "mensaje", mensaje,
            "destinatarioId", destinatarioId
        );
        try {
            restTemplate.postForEntity(notificationUrl + "/api/notificaciones", body, Void.class);
        } catch (Exception e) {
            System.err.println("Error al enviar notificacion: " + e.getMessage());
        }
    }
}
