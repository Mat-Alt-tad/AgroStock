package com.example.inventoryservice.domain.useCase;

import lombok.RequiredArgsConstructor;
import com.example.inventoryservice.domain.model.MovimientoInventario;
import com.example.inventoryservice.domain.model.gateway.MovimientoInventarioGateWay;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MovimientoInventarioUseCase {

    private final MovimientoInventarioGateWay movimientoInventarioGateWay;

    public MovimientoInventario save(MovimientoInventario movimiento) {
        try {
            return movimientoInventarioGateWay.save(movimiento);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar MovimientoInventario: " + e.getMessage());
        }
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
