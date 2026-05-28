package com.example.inventoryservice.infrastructure.driver_adapters.jpa_repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimientoInventarioDataJpaRepository extends JpaRepository<MovimientoInventarioData, String> {
    List<MovimientoInventarioData> findByStockItemId(String stockItemId);
}
