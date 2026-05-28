package com.example.inventoryservice.domain.useCase;

import lombok.RequiredArgsConstructor;
import com.example.inventoryservice.domain.model.MovimientoInventario;
import com.example.inventoryservice.domain.model.StockItem;
import com.example.inventoryservice.domain.model.gateway.MovimientoInventarioGateWay;
import com.example.inventoryservice.domain.model.gateway.StockItemGateWay;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MovimientoInventarioUseCase {

    private final MovimientoInventarioGateWay movimientoInventarioGateWay;
    private final StockItemGateWay stockItemGateWay;

    public MovimientoInventario save(MovimientoInventario movimiento) {
        try {
            MovimientoInventario saved = movimientoInventarioGateWay.save(movimiento);
            actualizarStock(movimiento);
            return saved;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar MovimientoInventario: " + e.getMessage());
        }
    }

    private void actualizarStock(MovimientoInventario movimiento) {
        StockItem stockItem = stockItemGateWay.findById(movimiento.getStockItemId())
                .orElseThrow(() -> new RuntimeException("StockItem no encontrado: " + movimiento.getStockItemId()));

        switch (movimiento.getTipo()) {
            case "ENTRADA":
                stockItem.setCantidad(stockItem.getCantidad() + movimiento.getCantidad());
                break;
            case "SALIDA":
                stockItem.setCantidad(Math.max(0, stockItem.getCantidad() - movimiento.getCantidad()));
                break;
            case "AJUSTE":
                stockItem.setCantidad(movimiento.getCantidad());
                break;
        }
        stockItemGateWay.save(stockItem);
    }

    public Optional<MovimientoInventario> findById(String id) {
        try {
            return movimientoInventarioGateWay.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar MovimientoInventario: " + e.getMessage());
        }
    }

    public List<MovimientoInventario> findAll() {
        try {
            return movimientoInventarioGateWay.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar MovimientosInventario: " + e.getMessage());
        }
    }

    public List<MovimientoInventario> findByStockItemId(String stockItemId) {
        try {
            return movimientoInventarioGateWay.findByStockItemId(stockItemId);
        } catch (Exception e) {
            throw new RuntimeException("Error al filtrar movimientos: " + e.getMessage());
        }
    }

    public void deleteById(String id) {
        try {
            movimientoInventarioGateWay.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar MovimientoInventario: " + e.getMessage());
        }
    }
}
