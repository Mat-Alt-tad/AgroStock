package com.example.inventoryservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventario {
    private String id;
    private String stockItemId;
    private String tipo; // ENTRADA, SALIDA, TRASLADO, AJUSTE
    private Integer cantidad;
    private LocalDateTime fecha;
}
