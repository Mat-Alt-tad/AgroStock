package com.example.inventoryservice.domain.useCase;

import lombok.RequiredArgsConstructor;
import com.example.inventoryservice.domain.model.StockItem;
import com.example.inventoryservice.domain.model.gateway.ProductoExistsGateway;
import com.example.inventoryservice.domain.model.gateway.StockItemGateWay;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class StockItemUseCase {

    private final StockItemGateWay stockItemGateWay;
    private final ProductoExistsGateway productoExistsGateway;

    public StockItem save(StockItem stockItem, String token) {
        try {
            if (!productoExistsGateway.existeProducto(stockItem.getProductoId(), token)) {
                throw new RuntimeException("El producto no existe en ProductService");
            }
            return stockItemGateWay.save(stockItem);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar StockItem: " + e.getMessage());
        }
    }

    public Optional<StockItem> findById(String id) {
        try {
            return stockItemGateWay.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar StockItem: " + e.getMessage());
        }
    }

    public Optional<StockItem> findByProductoId(String productoId) {
        try {
            return stockItemGateWay.findByProductoId(productoId);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar StockItem por productoId: " + e.getMessage());
        }
    }

    public List<StockItem> findAll() {
        try {
            return stockItemGateWay.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar StockItems: " + e.getMessage());
        }
    }

    public void deleteById(String id) {
        try {
            stockItemGateWay.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar StockItem: " + e.getMessage());
        }
    }
}
