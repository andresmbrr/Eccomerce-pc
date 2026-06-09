package example.ms_pedidos.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import example.ms_pedidos.dto.PaymentResponseDTO;

@FeignClient(name = "ms-pagos")
public interface PaymentClient {

    @GetMapping("/api/pagos/order/{orderId}")
    List<PaymentResponseDTO> obtenerPagosPorPedido(
            @PathVariable("orderId") Long orderId);
}