package com.example.inventoryservice.infrastructure.driver_adapters.jpa_repository;

import lombok.RequiredArgsConstructor;
import com.example.inventoryservice.domain.model.MovimientoInventario;
import com.example.inventoryservice.domain.model.gateway.MovimientoInventarioGateWay;
import com.example.inventoryservice.infrastructure.mapper.MovimientoInventarioMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MovimientoInventarioDataGatewayImpl implements MovimientoInventarioGateWay {

    private final MovimientoInventarioDataJpaRepository repository;
    private final MovimientoInventarioMapper mapper;

    @Override
    public MovimientoInventario save(MovimientoInventario movimiento) {
        return mapper.toDomain(repository.save(mapper.toData(movimiento)));
    }

    @Override
    public Optional<MovimientoInventario> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<MovimientoInventario> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoInventario> findByStockItemId(String stockItemId) {
        return repository.findByStockItemId(stockItemId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
