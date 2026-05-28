package com.example.inventoryservice.infrastructure.entity_points;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockItemResponse {
    private String id;
    private String productoId;
    private Integer cantidad;
    private Integer nivelMinimo;
}
