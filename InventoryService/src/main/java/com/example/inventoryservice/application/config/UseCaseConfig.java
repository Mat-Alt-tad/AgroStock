package com.example.inventoryservice.application.config;

import com.example.inventoryservice.domain.model.gateway.MovimientoInventarioGateWay;
import com.example.inventoryservice.domain.model.gateway.ProductoExistsGateway;
import com.example.inventoryservice.domain.model.gateway.StockItemGateWay;
import com.example.inventoryservice.domain.useCase.MovimientoInventarioUseCase;
import com.example.inventoryservice.domain.useCase.StockItemUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class UseCaseConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public StockItemUseCase stockItemUseCase(StockItemGateWay stockItemGateWay,
                                              ProductoExistsGateway productoExistsGateway) {
        return new StockItemUseCase(stockItemGateWay, productoExistsGateway);
    }

    @Bean
    public MovimientoInventarioUseCase movimientoInventarioUseCase(MovimientoInventarioGateWay movimientoInventarioGateWay) {
        return new MovimientoInventarioUseCase(movimientoInventarioGateWay);
    }
}
