package com.example.inventoryservice.domain.model.gateway;

import com.example.inventoryservice.domain.model.MovimientoInventario;
import java.util.List;
import java.util.Optional;

public interface MovimientoInventarioGateWay {
    MovimientoInventario save(MovimientoInventario movimiento);
    Optional<MovimientoInventario> findById(String id);
    List<MovimientoInventario> findAll();
    List<MovimientoInventario> findByStockItemId(String stockItemId);
    void deleteById(String id);
}
