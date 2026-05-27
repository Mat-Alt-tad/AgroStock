package com.example.inventoryservice.domain.model.gateway;

public interface ProductoExistsGateway {
    boolean existeProducto(String productoId, String token);
}
