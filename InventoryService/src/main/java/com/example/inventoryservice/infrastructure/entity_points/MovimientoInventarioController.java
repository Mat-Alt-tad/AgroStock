package com.example.inventoryservice.infrastructure.entity_points;

import lombok.RequiredArgsConstructor;
import com.example.inventoryservice.domain.model.MovimientoInventario;
import com.example.inventoryservice.domain.useCase.MovimientoInventarioUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory/movimientos")
@RequiredArgsConstructor
public class MovimientoInventarioController {

    private final MovimientoInventarioUseCase movimientoInventarioUseCase;

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> findAll() {
        List<MovimientoResponse> lista = movimientoInventarioUseCase.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> findById(@PathVariable String id) {
        return movimientoInventarioUseCase.findById(id)
                .map(m -> ResponseEntity.ok(toResponse(m)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stock/{stockItemId}")
    public ResponseEntity<List<MovimientoResponse>> findByStockItemId(@PathVariable String stockItemId) {
        List<MovimientoResponse> lista = movimientoInventarioUseCase.findByStockItemId(stockItemId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> create(@RequestBody MovimientoRequest request) {
        MovimientoInventario saved = movimientoInventarioUseCase.save(toDomain(request));
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        movimientoInventarioUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private MovimientoInventario toDomain(MovimientoRequest req) {
        return MovimientoInventario.builder()
                .stockItemId(req.getStockItemId())
                .tipo(req.getTipo())
                .cantidad(req.getCantidad())
                .fecha(LocalDateTime.now())
                .build();
    }

    private MovimientoResponse toResponse(MovimientoInventario m) {
        return MovimientoResponse.builder()
                .id(m.getId())
                .stockItemId(m.getStockItemId())
                .tipo(m.getTipo())
                .cantidad(m.getCantidad())
                .fecha(m.getFecha())
                .build();
    }
}
