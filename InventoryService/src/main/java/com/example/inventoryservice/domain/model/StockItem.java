package com.example.inventoryservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockItem {
    private String id;
    private String productoId;
    private Integer cantidad;
    private Integer nivelMinimo;
}
