package com.example.inventoryservice.infrastructure.entity_points;

import lombok.Data;

@Data
public class SalidaRequest {
    private String productoId;
    private Integer cantidad;
}
