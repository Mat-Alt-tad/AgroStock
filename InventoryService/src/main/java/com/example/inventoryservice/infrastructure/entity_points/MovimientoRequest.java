package com.example.inventoryservice.infrastructure.entity_points;

import lombok.Data;

@Data
public class MovimientoRequest {
    private String stockItemId;
    private String tipo;
    private Integer cantidad;
}
