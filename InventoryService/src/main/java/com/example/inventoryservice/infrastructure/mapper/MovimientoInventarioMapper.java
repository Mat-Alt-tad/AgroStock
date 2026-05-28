package com.example.inventoryservice.infrastructure.mapper;

import com.example.inventoryservice.domain.model.MovimientoInventario;
import com.example.inventoryservice.infrastructure.driver_adapters.jpa_repository.MovimientoInventarioData;
import org.springframework.stereotype.Component;

@Component
public class MovimientoInventarioMapper {

    public MovimientoInventario toDomain(MovimientoInventarioData data) {
        if (data == null) return null;
        return MovimientoInventario.builder()
                .id(data.getId())
                .stockItemId(data.getStockItemId())
                .tipo(data.getTipo())
                .cantidad(data.getCantidad())
                .fecha(data.getFecha())
                .build();
    }

    public MovimientoInventarioData toData(MovimientoInventario domain) {
        if (domain == null) return null;
        return MovimientoInventarioData.builder()
                .id(domain.getId())
                .stockItemId(domain.getStockItemId())
                .tipo(domain.getTipo())
                .cantidad(domain.getCantidad())
                .fecha(domain.getFecha())
                .build();
    }
}
