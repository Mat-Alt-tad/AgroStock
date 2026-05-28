package com.example.inventoryservice.infrastructure.entity_points;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MovimientoResponse {
    private String id;
    private String stockItemId;
    private String tipo;
    private Integer cantidad;
    private LocalDateTime fecha;
}
