package com.example.inventoryservice.infrastructure.entity_points;

import lombok.Data;

@Data
public class StockItemRequest {
    private String productoId;
    private Integer cantidad;
    private Integer nivelMinimo;
}
