package com.example.inventoryservice.domain.model.gateway;

import com.example.inventoryservice.domain.model.StockItem;
import java.util.List;
import java.util.Optional;

public interface StockItemGateWay {
    StockItem save(StockItem stockItem);
    Optional<StockItem> findById(String id);
    Optional<StockItem> findByProductoId(String productoId);
    List<StockItem> findAll();
    void deleteById(String id);
}
