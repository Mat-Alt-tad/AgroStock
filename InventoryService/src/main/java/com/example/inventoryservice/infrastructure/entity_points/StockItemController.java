package com.example.inventoryservice.infrastructure.entity_points;

import lombok.RequiredArgsConstructor;
import com.example.inventoryservice.domain.model.StockItem;
import com.example.inventoryservice.domain.useCase.StockItemUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory/stock")
@RequiredArgsConstructor
public class StockItemController {

    private final StockItemUseCase stockItemUseCase;

    @GetMapping
    public ResponseEntity<List<StockItemResponse>> findAll() {
        List<StockItemResponse> lista = stockItemUseCase.findAll().stream()
                .map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockItemResponse> findById(@PathVariable String id) {
        return stockItemUseCase.findById(id)
                .map(s -> ResponseEntity.ok(toResponse(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<StockItemResponse> findByProductoId(@PathVariable String productoId) {
        return stockItemUseCase.findByProductoId(productoId)
                .map(s -> ResponseEntity.ok(toResponse(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StockItemResponse> create(
            @RequestBody StockItemRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        StockItem saved = stockItemUseCase.save(toDomain(request), token);
        return ResponseEntity.ok(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockItemResponse> update(
            @PathVariable String id,
            @RequestBody StockItemRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        StockItem domain = toDomain(request);
        domain.setId(id);
        return ResponseEntity.ok(toResponse(stockItemUseCase.save(domain, token)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        stockItemUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private StockItem toDomain(StockItemRequest req) {
        return StockItem.builder()
                .productoId(req.getProductoId())
                .cantidad(req.getCantidad())
                .nivelMinimo(req.getNivelMinimo())
                .build();
    }

    private StockItemResponse toResponse(StockItem s) {
        return StockItemResponse.builder()
                .id(s.getId())
                .productoId(s.getProductoId())
                .cantidad(s.getCantidad())
                .nivelMinimo(s.getNivelMinimo())
                .build();
    }
}
