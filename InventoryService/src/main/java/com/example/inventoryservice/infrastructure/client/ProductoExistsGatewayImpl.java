package com.example.inventoryservice.infrastructure.client;

import lombok.RequiredArgsConstructor;
import com.example.inventoryservice.domain.model.gateway.ProductoExistsGateway;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductoExistsGatewayImpl implements ProductoExistsGateway {

    private final ProductClient productClient;

    @Override
    public boolean existeProducto(String productoId, String token) {
        return productClient.existeProducto(productoId, token);
    }
}
