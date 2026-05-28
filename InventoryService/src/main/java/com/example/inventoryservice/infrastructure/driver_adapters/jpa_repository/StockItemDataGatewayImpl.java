package com.example.inventoryservice.infrastructure.driver_adapters.jpa_repository;

import lombok.RequiredArgsConstructor;
import com.example.inventoryservice.domain.model.StockItem;
import com.example.inventoryservice.domain.model.gateway.StockItemGateWay;
import com.example.inventoryservice.infrastructure.mapper.StockItemMapper;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StockItemDataGatewayImpl implements StockItemGateWay {

    private final StockItemDataJpaRepository repository;
    private final StockItemMapper mapper;

    @Override
    public StockItem save(StockItem stockItem) {
        return mapper.toDomain(repository.save(mapper.toData(stockItem)));
    }

    @Override
    public Optional<StockItem> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<StockItem> findByProductoId(String productoId) {
        return repository.findByProductoId(productoId).map(mapper::toDomain);
    }

    @Override
    public List<StockItem> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
