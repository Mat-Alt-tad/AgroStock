package com.example.inventoryservice.infrastructure.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ProductClient {

    private final RestTemplate restTemplate;

    @Value("${services.product-url}")
    private String productUrl;

    public boolean existeProducto(String productoId, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            restTemplate.exchange(
                productUrl + "/api/productos/" + productoId,
                HttpMethod.GET,
                entity,
                Object.class
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
