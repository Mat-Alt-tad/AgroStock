package com.example.inventoryservice.infrastructure.mapper;

import com.example.inventoryservice.domain.model.StockItem;
import com.example.inventoryservice.infrastructure.driver_adapters.jpa_repository.StockItemData;
import org.springframework.stereotype.Component;

@Component
public class StockItemMapper {

    public StockItem toDomain(StockItemData data) {
        if (data == null) return null;
        return StockItem.builder()
                .id(data.getId())
                .productoId(data.getProductoId())
                .cantidad(data.getCantidad())
                .nivelMinimo(data.getNivelMinimo())
                .build();
    }

    public StockItemData toData(StockItem domain) {
        if (domain == null) return null;
        return StockItemData.builder()
                .id(domain.getId())
                .productoId(domain.getProductoId())
                .cantidad(domain.getCantidad())
                .nivelMinimo(domain.getNivelMinimo())
                .build();
    }
}
