# Plan: Comunicación Orden → Inventario

## 1. InventoryService — Vincular movimientos con stock real

### a) `MovimientoInventarioUseCase.java` — Inyectar StockItemGateWay y ajustar stock

**Ruta:** `InventoryService/src/main/java/com/example/inventoryservice/domain/useCase/MovimientoInventarioUseCase.java`

**Cambio:** Agregar `StockItemGateWay` como dependencia. En `save()`, después de guardar el movimiento, buscar el `StockItem` por `stockItemId` y ajustar la cantidad según el tipo de movimiento.

```java
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

    // ... resto de métodos igual (findById, findAll, findByStockItemId, deleteById)
}
```

### b) `UseCaseConfig.java` — Inyectar StockItemGateWay

**Ruta:** `InventoryService/src/main/java/com/example/inventoryservice/application/config/UseCaseConfig.java`

**Cambio:** Pasar `StockItemGateWay` al constructor de `MovimientoInventarioUseCase`.

```java
@Bean
public MovimientoInventarioUseCase movimientoInventarioUseCase(
        MovimientoInventarioGateWay movimientoInventarioGateWay,
        StockItemGateWay stockItemGateWay) {
    return new MovimientoInventarioUseCase(movimientoInventarioGateWay, stockItemGateWay);
}
```

### c) `MovimientoInventarioController.java` — Nuevo endpoint POST /salida

**Ruta:** `InventoryService/src/main/java/com/example/inventoryservice/infrastructure/entity_points/MovimientoInventarioController.java`

**Cambio:** Agregar endpoint que acepte `productoId` + `cantidad`, busque el StockItem por productoId y cree un movimiento SALIDA.

Agregar este método al controller existente:

```java
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
    return ResponseEntity.ok(toResponse(saved));
}
```

**Nota:** También necesitas inyectar `StockItemUseCase` en el controller. Agregar el campo:

```java
private final StockItemUseCase stockItemUseCase;
```

Y crear el DTO `SalidaRequest`:

```java
@Data
public class SalidaRequest {
    private String productoId;
    private Integer cantidad;
}
```

---

## 2. PurchaseOrderService — Reducir stock al aprobar orden

### a) Crear `InventorySalidaGateway.java`

**Ruta:** `PurchaseOrderService/src/main/java/com/example/purchaseorderservice/domain/model/gateway/InventorySalidaGateway.java`

```java
package com.example.purchaseorderservice.domain.model.gateway;

public interface InventorySalidaGateway {
    void registrarSalida(String productoId, Integer cantidad, String token);
}
```

### b) `InventoryClient.java` — Agregar método registrarSalida

**Ruta:** `PurchaseOrderService/src/main/java/com/example/purchaseorderservice/infrastructure/client/InventoryClient.java`

**Cambio:** Agregar método `registrarSalida` que llame al nuevo endpoint del InventoryService.

```java
public void registrarSalida(String productoId, Integer cantidad, String token) {
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");
        Map<String, Object> body = new java.util.HashMap<>();
        body.put("productoId", productoId);
        body.put("cantidad", cantidad);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        restTemplate.postForObject(
            inventoryUrl + "/api/inventory/movimientos/salida",
            entity,
            Object.class
        );
    } catch (Exception e) {
        throw new RuntimeException("Error al registrar salida en inventario: " + e.getMessage());
    }
}
```

### c) `OrdenCompraUseCase.java` — Detectar cambio a APROBADA y reducir stock

**Ruta:** `PurchaseOrderService/src/main/java/com/example/purchaseorderservice/domain/useCase/OrdenCompraUseCase.java`

**Cambio:** Inyectar `LineaOrdenGateWay` e `InventorySalidaGateway`. Modificar `save()` para aceptar `token`, detectar cambio de estado a APROBADA, y reducir stock.

```java
package com.example.purchaseorderservice.domain.useCase;

import lombok.RequiredArgsConstructor;
import com.example.purchaseorderservice.domain.model.LineaOrden;
import com.example.purchaseorderservice.domain.model.OrdenCompra;
import com.example.purchaseorderservice.domain.model.gateway.InventorySalidaGateway;
import com.example.purchaseorderservice.domain.model.gateway.LineaOrdenGateWay;
import com.example.purchaseorderservice.domain.model.gateway.OrdenCompraGateWay;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrdenCompraUseCase {

    private final OrdenCompraGateWay ordenCompraGateWay;
    private final LineaOrdenGateWay lineaOrdenGateWay;
    private final InventorySalidaGateway inventorySalidaGateway;

    public OrdenCompra save(OrdenCompra ordenCompra, String token) {
        try {
            boolean aprobando = false;

            if (ordenCompra.getId() != null) {
                Optional<OrdenCompra> existing = ordenCompraGateWay.findById(ordenCompra.getId());
                if (existing.isPresent()
                        && !"APROBADA".equals(existing.get().getEstado())
                        && "APROBADA".equals(ordenCompra.getEstado())) {
                    aprobando = true;
                }
            }

            OrdenCompra saved = ordenCompraGateWay.save(ordenCompra);

            if (aprobando) {
                List<LineaOrden> lineas = lineaOrdenGateWay.findByOrdenId(saved.getId());
                for (LineaOrden linea : lineas) {
                    inventorySalidaGateway.registrarSalida(
                        linea.getProductoId(), linea.getCantidad(), token);
                }
            }

            return saved;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar OrdenCompra: " + e.getMessage());
        }
    }

    // ... resto de métodos igual
}
```

### d) `OrdenCompraController.java` — Pasar token a save()

**Ruta:** `PurchaseOrderService/src/main/java/com/example/purchaseorderservice/infrastructure/entity_points/OrdenCompraController.java`

**Cambio:** Los métodos `create` y `update` deben recibir el header `Authorization` y pasarlo al use case.

```java
@PostMapping
public ResponseEntity<OrdenCompraResponse> create(
        @RequestBody OrdenCompraRequest request,
        @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    OrdenCompra saved = ordenCompraUseCase.save(toDomain(request), token);
    return ResponseEntity.ok(toResponse(saved));
}

@PutMapping("/{id}")
public ResponseEntity<OrdenCompraResponse> update(
        @PathVariable String id,
        @RequestBody OrdenCompraRequest request,
        @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    OrdenCompra domain = toDomain(request);
    domain.setId(id);
    return ResponseEntity.ok(toResponse(ordenCompraUseCase.save(domain, token)));
}
```

### e) `UseCaseConfig.java` — Inyectar nuevas dependencias

**Ruta:** `PurchaseOrderService/src/main/java/com/example/purchaseorderservice/application/config/UseCaseConfig.java`

**Cambio:**

```java
@Bean
public OrdenCompraUseCase ordenCompraUseCase(
        OrdenCompraGateWay ordenCompraGateWay,
        LineaOrdenGateWay lineaOrdenGateWay,
        InventorySalidaGateway inventorySalidaGateway) {
    return new OrdenCompraUseCase(ordenCompraGateWay, lineaOrdenGateWay, inventorySalidaGateway);
}
```

---

## 3. (Opcional) Notificar stock bajo

Cuando `stockItem.getCantidad() < stockItem.getNivelMinimo()` en `MovimientoInventarioUseCase.actualizarStock()`, se podría llamar a NotificationService para alertar. Esto quedaría para una fase posterior.

---

## Flujo final

```
Cliente → PUT /api/ordenes/{id} { "estado": "APROBADA" }
  → OrdenCompraController.update()
    → OrdenCompraUseCase.save(orden, token)
      → Detecta: PENDIENTE → APROBADA
      → Busca LineaOrden por ordenId
      → Por cada línea:
          InventoryClient.registrarSalida(productoId, cantidad, token)
            → POST /api/inventory/movimientos/salida { productoId, cantidad }
              → StockItemUseCase.findByProductoId(productoId)
              → Crea MovimientoInventario (tipo=SALIDA)
              → MovimientoInventarioUseCase.save()
                → Guarda movimiento
                → StockItem.cantidad -= cantidad (mínimo 0)
```
