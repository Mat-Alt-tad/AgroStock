package com.example.inventoryservice.infrastructure.entity_points;

import lombok.RequiredArgsConstructor;
import com.example.inventoryservice.domain.model.MovimientoInventario;
import com.example.inventoryservice.domain.model.StockItem;
import com.example.inventoryservice.domain.useCase.MovimientoInventarioUseCase;
import com.example.inventoryservice.domain.useCase.StockItemUseCase;
import com.example.inventoryservice.infrastructure.notifier.NotificationClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory/movimientos")
@RequiredArgsConstructor
public class MovimientoInventarioController {

    private final MovimientoInventarioUseCase movimientoInventarioUseCase;
    private final StockItemUseCase stockItemUseCase;
    private final NotificationClient notificationClient;

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
        verificarStockBajo(saved.getStockItemId());
        return ResponseEntity.ok(toResponse(saved));
    }

    @PostMapping("/salida")
    public ResponseEntity<MovimientoResponse> registrarSalida(
            @RequestBody SalidaRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        StockItem stockItem = stockItemUseCase.findByProductoId(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("StockItem no encontrado para producto: " + request.getProductoId()));

        MovimientoInventario movimiento = MovimientoInventario.builder()
                .stockItemId(stockItem.getId())
                .tipo("SALIDA")
                .cantidad(request.getCantidad())
                .fecha(LocalDateTime.now())
                .build();

        MovimientoInventario saved = movimientoInventarioUseCase.save(movimiento);
        verificarStockBajo(saved.getStockItemId());
        return ResponseEntity.ok(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        movimientoInventarioUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void verificarStockBajo(String stockItemId) {
        try {
            StockItem stockItem = stockItemUseCase.findById(stockItemId).orElse(null);
            if (stockItem == null) return;

            if (stockItem.getCantidad() < stockItem.getNivelMinimo()) {
                String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

                String mensaje = "El producto " + stockItem.getProductoId()
                        + " está por debajo del nivel mínimo. "
                        + "Stock actual: " + stockItem.getCantidad()
                        + ", Mínimo: " + stockItem.getNivelMinimo();

                notificationClient.enviarNotificacion(
                        "ALERTA",
                        "Stock Bajo",
                        userEmail,
                        mensaje,
                        stockItem.getProductoId()
                );
            }
        } catch (Exception e) {
            System.err.println("Error al verificar stock bajo: " + e.getMessage());
        }
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
