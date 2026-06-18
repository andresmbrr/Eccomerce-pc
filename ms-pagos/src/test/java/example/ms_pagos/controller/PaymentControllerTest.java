package example.ms_pagos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import example.ms_pagos.dto.PaymentRequestDTO;
import example.ms_pagos.dto.PaymentResponseDTO;
import example.ms_pagos.model.PaymentStatus;
import example.ms_pagos.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        PaymentController paymentController =
                new PaymentController(paymentService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(paymentController)
                .build();
    }

    @Test
    void getAllPayments_DeberiaRetornar200() throws Exception {

        // ARRANGE: preparar datos y mocks.

        PaymentResponseDTO payment =
                PaymentResponseDTO.builder()
                        .id(1L)
                        .orderId(100L)
                        .amount(new BigDecimal("15000"))
                        .paymentMethod("TARJETA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(LocalDateTime.now())
                        .active(true)
                        .build();

        when(paymentService.getAllPayments())
                .thenReturn(List.of(payment));

        // ACT: ejecutar endpoint.

        mockMvc.perform(get("/api/pagos"))

                // ASSERT: verificar resultado esperado.
                .andExpect(status().isOk());

        // VERIFY: comprobar llamadas al mock.
        verify(paymentService).getAllPayments();

        /*
         * Caso hipotético de falla para QA:
         *
         * Se esperaba:
         * HTTP 200 OK
         *
         * Se obtuvo:
         * HTTP 500 Internal Server Error
         *
         * Revisar:
         * - configuración del controller
         * - mapping del endpoint
         * - excepciones no controladas
         * - respuesta entregada por el service
         */
    }
        @Test
    void getPaymentById_DeberiaRetornar200() throws Exception {

        // ARRANGE: preparar datos y mocks.

        PaymentResponseDTO payment =
                PaymentResponseDTO.builder()
                        .id(1L)
                        .orderId(100L)
                        .amount(new BigDecimal("15000"))
                        .paymentMethod("TARJETA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(LocalDateTime.now())
                        .active(true)
                        .build();

        when(paymentService.getPaymentById(1L))
                .thenReturn(payment);

        // ACT: ejecutar endpoint.

        mockMvc.perform(get("/api/pagos/1"))

                // ASSERT: verificar resultado esperado.
                .andExpect(status().isOk());

        // VERIFY: comprobar llamadas al mock.
        verify(paymentService).getPaymentById(1L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * HTTP 200 OK
        *
        * Se obtuvo:
        * HTTP 404 Not Found
        *
        * Revisar:
        * - existencia del pago
        * - lógica del service
        * - manejo de ResourceNotFoundException
        */
    }
        @Test
    void getPaymentsByOrderId_DeberiaRetornar200() throws Exception {

        // ARRANGE: preparar datos y mocks.

        PaymentResponseDTO payment =
                PaymentResponseDTO.builder()
                        .id(1L)
                        .orderId(100L)
                        .amount(new BigDecimal("15000"))
                        .paymentMethod("TARJETA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(LocalDateTime.now())
                        .active(true)
                        .build();

        when(paymentService.getPaymentsByOrderId(100L))
                .thenReturn(List.of(payment));

        // ACT: ejecutar endpoint.

        mockMvc.perform(get("/api/pagos/order/100"))

                // ASSERT: verificar resultado esperado.
                .andExpect(status().isOk());

        // VERIFY: comprobar llamadas al mock.
        verify(paymentService).getPaymentsByOrderId(100L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * HTTP 200 OK
        *
        * Se obtuvo:
        * HTTP 404 Not Found
        *
        * Revisar:
        * - mapping del endpoint
        * - parámetro orderId
        * - implementación del service
        */
    }
        @Test
    void createPayment_DeberiaRetornar201() throws Exception {

        // ARRANGE: preparar datos y mocks.

        PaymentRequestDTO request =
                new PaymentRequestDTO(
                        100L,
                        new BigDecimal("15000"),
                        "TARJETA"
                );

        PaymentResponseDTO response =
                PaymentResponseDTO.builder()
                        .id(1L)
                        .orderId(100L)
                        .amount(new BigDecimal("15000"))
                        .paymentMethod("TARJETA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(LocalDateTime.now())
                        .active(true)
                        .build();

        when(paymentService.createPayment(any(PaymentRequestDTO.class)))
                .thenReturn(response);

        ObjectMapper mapper = new ObjectMapper();

        // ACT: ejecutar endpoint.

        mockMvc.perform(
                        post("/api/pagos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )

                // ASSERT: verificar resultado esperado.
                .andExpect(status().isCreated());

        // VERIFY: comprobar llamadas al mock.
        verify(paymentService)
                .createPayment(any(PaymentRequestDTO.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * HTTP 201 Created
        *
        * Se obtuvo:
        * HTTP 400 Bad Request
        *
        * Revisar:
        * - validaciones del DTO
        * - campos obligatorios
        * - formato JSON enviado
        */
    }
        @Test
    void updatePayment_DeberiaRetornar200() throws Exception {

        // ARRANGE: preparar datos y mocks.

        PaymentRequestDTO request =
                new PaymentRequestDTO(
                        100L,
                        new BigDecimal("20000"),
                        "TRANSFERENCIA"
                );

        PaymentResponseDTO response =
                PaymentResponseDTO.builder()
                        .id(1L)
                        .orderId(100L)
                        .amount(new BigDecimal("20000"))
                        .paymentMethod("TRANSFERENCIA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(LocalDateTime.now())
                        .active(true)
                        .build();

        when(paymentService.updatePayment(
                eq(1L),
                any(PaymentRequestDTO.class)))
                .thenReturn(response);

        ObjectMapper mapper = new ObjectMapper();

        // ACT: ejecutar endpoint.

        mockMvc.perform(
                        put("/api/pagos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )

                // ASSERT: verificar resultado esperado.
                .andExpect(status().isOk());

        // VERIFY: comprobar llamadas al mock.
        verify(paymentService)
                .updatePayment(
                        eq(1L),
                        any(PaymentRequestDTO.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * HTTP 200 OK
        *
        * Se obtuvo:
        * HTTP 404 Not Found
        *
        * Revisar:
        * - existencia del pago
        * - lógica updatePayment()
        * - ResourceNotFoundException
        */
    }
        @Test
    void deletePayment_DeberiaRetornar204() throws Exception {

        // ARRANGE: preparar datos y mocks.
        // No es necesario configurar when(...)
        // porque deletePayment retorna void.

        // ACT: ejecutar endpoint.

        mockMvc.perform(delete("/api/pagos/1"))

                // ASSERT: verificar resultado esperado.
                .andExpect(status().isNoContent());

        // VERIFY: comprobar llamadas al mock.
        verify(paymentService).deletePayment(1L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * HTTP 204 No Content
        *
        * Se obtuvo:
        * HTTP 404 Not Found
        *
        * Revisar:
        * - existencia del pago
        * - implementación deletePayment()
        * - manejo de ResourceNotFoundException
        */
    }
}