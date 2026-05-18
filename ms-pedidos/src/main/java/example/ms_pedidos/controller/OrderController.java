package example.ms_pedidos.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.ms_pedidos.dto.OrderRequestDTO;
import example.ms_pedidos.dto.OrderResponseDTO;
import example.ms_pedidos.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderResponseDTO>
    createOrder(
            @Valid @RequestBody OrderRequestDTO dto){

        log.info("POST /api/orders ejecutado");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createOrder(dto));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>>
    getAllOrders(){

        log.info("GET /api/orders ejecutado");

        return ResponseEntity.ok(
                service.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO>
    getOrderById(@PathVariable Long id){

        log.info("GET /api/orders/{} ejecutado", id);

        return ResponseEntity.ok(
                service.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>>
    getOrdersByUserId(
            @PathVariable Long userId){

        log.info("GET pedidos usuario {}",
                userId);

        return ResponseEntity.ok(
                service.getOrdersByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO>
    updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequestDTO dto){

        log.info("PUT /api/orders/{} ejecutado", id);

        return ResponseEntity.ok(
                service.updateOrder(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteOrder(@PathVariable Long id){

        log.info("DELETE /api/orders/{} ejecutado", id);

        service.deleteOrder(id);

        return ResponseEntity.noContent().build();
    }
}