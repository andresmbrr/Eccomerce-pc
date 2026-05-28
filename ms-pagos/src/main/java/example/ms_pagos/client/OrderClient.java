package example.ms_pagos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import example.ms_pagos.dto.OrderResponseDTO;

@FeignClient(name = "ms-pedidos")
public interface OrderClient {

    @GetMapping("/api/pedidos/{id}")
    OrderResponseDTO getOrderById(
            @PathVariable Long id);
}