package example.ms_pagos.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import example.ms_pagos.client.OrderClient;
import example.ms_pagos.dto.OrderResponseDTO;
import example.ms_pagos.dto.PaymentRequestDTO;
import example.ms_pagos.dto.PaymentResponseDTO;
import example.ms_pagos.exception.ResourceNotFoundException;
import example.ms_pagos.model.Payment;
import example.ms_pagos.model.PaymentStatus;
import example.ms_pagos.repository.PaymentRepository;
import example.ms_pagos.service.impl.PaymentServiceImpl;



@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository repository;

    @Mock
    private OrderClient orderClient;

    @InjectMocks
    private PaymentServiceImpl service;


        @Test
    void createPayment_DeberiaCrearPagoCorrectamente() {

        // ARRANGE: preparar datos y mocks.

        PaymentRequestDTO request =
                new PaymentRequestDTO(
                        100L,
                        new BigDecimal("15000"),
                        "TARJETA"
                );

        OrderResponseDTO order =
                new OrderResponseDTO();

        order.setId(100L);

        Payment savedPayment =
                Payment.builder()
                        .id(1L)
                        .orderId(100L)
                        .amount(new BigDecimal("15000"))
                        .paymentMethod("TARJETA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(LocalDateTime.now())
                        .active(true)
                        .build();

        when(orderClient.getOrderById(100L))
                .thenReturn(order);

        when(repository.save(any(Payment.class)))
                .thenReturn(savedPayment);

        // ACT: ejecutar método.

        PaymentResponseDTO result =
                service.createPayment(request);

        // ASSERT: verificar resultado esperado.

        assertNotNull(result);

        assertEquals(1L, result.getId());

        assertEquals(
                PaymentStatus.APPROVED,
                result.getStatus());

        // VERIFY: comprobar llamadas al mock.

        verify(orderClient)
                .getOrderById(100L);

        verify(repository)
                .save(any(Payment.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Pago creado con estado APPROVED
        *
        * Se obtuvo:
        * Estado incorrecto o DTO nulo
        *
        * Revisar:
        * - integración con OrderClient
        * - persistencia en repository
        * - mapeo a DTO
        */
    }
        @Test
    void getAllPayments_DeberiaRetornarSoloPagosActivos() {

        // ARRANGE: preparar datos y mocks.

        Payment activePayment =
                Payment.builder()
                        .id(1L)
                        .orderId(100L)
                        .amount(new BigDecimal("15000"))
                        .paymentMethod("TARJETA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(LocalDateTime.now())
                        .active(true)
                        .build();

        Payment inactivePayment =
                Payment.builder()
                        .id(2L)
                        .orderId(200L)
                        .amount(new BigDecimal("30000"))
                        .paymentMethod("TRANSFERENCIA")
                        .status(PaymentStatus.REJECTED)
                        .paymentDate(LocalDateTime.now())
                        .active(false)
                        .build();

        when(repository.findAll())
                .thenReturn(List.of(activePayment, inactivePayment));

        // ACT: ejecutar método.

        List<PaymentResponseDTO> result =
                service.getAllPayments();

        // ASSERT: verificar resultado esperado.

        assertNotNull(result);

        assertEquals(1, result.size());

        assertEquals(
                1L,
                result.get(0).getId());

        // VERIFY: comprobar llamadas al mock.

        verify(repository)
                .findAll();

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * 1 pago activo
        *
        * Se obtuvo:
        * 2 pagos
        *
        * Revisar:
        * - filtro active == true
        * - transformación a DTO
        */
    }
        @Test
    void getPaymentById_DeberiaRetornarPagoExistente() {

        // ARRANGE: preparar datos y mocks.

        Payment payment =
                Payment.builder()
                        .id(1L)
                        .orderId(100L)
                        .amount(new BigDecimal("15000"))
                        .paymentMethod("TARJETA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(LocalDateTime.now())
                        .active(true)
                        .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(payment));

        // ACT: ejecutar método.

        PaymentResponseDTO result =
                service.getPaymentById(1L);

        // ASSERT: verificar resultado esperado.

        assertNotNull(result);

        assertEquals(1L, result.getId());

        assertEquals(100L, result.getOrderId());

        assertEquals(
                PaymentStatus.APPROVED,
                result.getStatus());

        // VERIFY: comprobar llamadas al mock.

        verify(repository)
                .findById(1L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Pago encontrado correctamente
        *
        * Se obtuvo:
        * DTO nulo o datos incorrectos
        *
        * Revisar:
        * - búsqueda por ID
        * - mapeo Payment -> DTO
        */
    }
        @Test
    void getPaymentById_DeberiaLanzarExcepcionCuandoNoExiste() {

        // ARRANGE: preparar datos y mocks.

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y verificar excepción.

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.getPaymentById(999L)
                );

        assertEquals(
                "Pago no encontrado con ID: 999",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        verify(repository)
                .findById(999L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * ResourceNotFoundException
        *
        * Se obtuvo:
        * DTO vacío o null
        *
        * Revisar:
        * - validación de existencia
        * - uso correcto de orElseThrow()
        */
    }
        @Test
    void getPaymentsByOrderId_DeberiaRetornarSoloPagosActivos() {

        // ARRANGE: preparar datos y mocks.

        Payment activePayment =
                Payment.builder()
                        .id(1L)
                        .orderId(100L)
                        .amount(new BigDecimal("15000"))
                        .paymentMethod("TARJETA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(LocalDateTime.now())
                        .active(true)
                        .build();

        Payment inactivePayment =
                Payment.builder()
                        .id(2L)
                        .orderId(100L)
                        .amount(new BigDecimal("20000"))
                        .paymentMethod("TRANSFERENCIA")
                        .status(PaymentStatus.REJECTED)
                        .paymentDate(LocalDateTime.now())
                        .active(false)
                        .build();

        when(repository.findByOrderId(100L))
                .thenReturn(List.of(activePayment, inactivePayment));

        // ACT: ejecutar método.

        List<PaymentResponseDTO> result =
                service.getPaymentsByOrderId(100L);

        // ASSERT: verificar resultado esperado.

        assertNotNull(result);

        assertEquals(1, result.size());

        assertEquals(
                1L,
                result.get(0).getId());

        assertEquals(
                100L,
                result.get(0).getOrderId());

        // VERIFY: comprobar llamadas al mock.

        verify(repository)
                .findByOrderId(100L);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * 1 pago activo
        *
        * Se obtuvo:
        * 2 pagos
        *
        * Revisar:
        * - filtro active == true
        * - búsqueda por orderId
        * - mapeo a DTO
        */
    }
        @Test
    void updatePayment_DeberiaActualizarPagoCorrectamente() {

        // ARRANGE: preparar datos y mocks.

        PaymentRequestDTO request =
                new PaymentRequestDTO(
                        200L,
                        new BigDecimal("25000"),
                        "TRANSFERENCIA"
                );

        Payment existingPayment =
                Payment.builder()
                        .id(1L)
                        .orderId(100L)
                        .amount(new BigDecimal("15000"))
                        .paymentMethod("TARJETA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(LocalDateTime.now())
                        .active(true)
                        .build();

        OrderResponseDTO order =
                new OrderResponseDTO();

        order.setId(200L);

        Payment updatedPayment =
                Payment.builder()
                        .id(1L)
                        .orderId(200L)
                        .amount(new BigDecimal("25000"))
                        .paymentMethod("TRANSFERENCIA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(existingPayment.getPaymentDate())
                        .active(true)
                        .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(existingPayment));

        when(orderClient.getOrderById(200L))
                .thenReturn(order);

        when(repository.save(any(Payment.class)))
                .thenReturn(updatedPayment);

        // ACT: ejecutar método.

        PaymentResponseDTO result =
                service.updatePayment(1L, request);

        // ASSERT: verificar resultado esperado.

        assertNotNull(result);

        assertEquals(1L, result.getId());

        assertEquals(200L, result.getOrderId());

        assertEquals(
                new BigDecimal("25000"),
                result.getAmount());

        assertEquals(
                "TRANSFERENCIA",
                result.getPaymentMethod());

        // VERIFY: comprobar llamadas al mock.

        verify(repository)
                .findById(1L);

        verify(orderClient)
                .getOrderById(200L);

        verify(repository)
                .save(any(Payment.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * Pago actualizado correctamente
        *
        * Se obtuvo:
        * Datos antiguos o actualización incompleta
        *
        * Revisar:
        * - actualización de campos
        * - validación con OrderClient
        * - persistencia mediante repository.save()
        */
    }
        @Test
    void updatePayment_DeberiaLanzarExcepcionCuandoPagoNoExiste() {

        // ARRANGE: preparar datos y mocks.

        PaymentRequestDTO request =
                new PaymentRequestDTO(
                        200L,
                        new BigDecimal("25000"),
                        "TRANSFERENCIA"
                );

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y verificar excepción.

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.updatePayment(999L, request)
                );

        assertEquals(
                "Pago no encontrado con ID: 999",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        verify(repository)
                .findById(999L);

        verify(orderClient, never())
                .getOrderById(anyLong());

        verify(repository, never())
                .save(any(Payment.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * ResourceNotFoundException
        *
        * Se obtuvo:
        * Actualización realizada o excepción incorrecta
        *
        * Revisar:
        * - validación de existencia
        * - uso de orElseThrow()
        * - flujo de actualización
        */
    }
        @Test
    void deletePayment_DeberiaRealizarEliminacionLogica() {

        // ARRANGE: preparar datos y mocks.

        Payment payment =
                Payment.builder()
                        .id(1L)
                        .orderId(100L)
                        .amount(new BigDecimal("15000"))
                        .paymentMethod("TARJETA")
                        .status(PaymentStatus.APPROVED)
                        .paymentDate(LocalDateTime.now())
                        .active(true)
                        .build();

        when(repository.findById(1L))
                .thenReturn(Optional.of(payment));

        when(repository.save(any(Payment.class)))
                .thenReturn(payment);

        // ACT: ejecutar método.

        service.deletePayment(1L);

        // ASSERT: verificar resultado esperado.

        assertFalse(payment.getActive());

        // VERIFY: comprobar llamadas al mock.

        verify(repository)
                .findById(1L);

        verify(repository)
                .save(payment);

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * active = false
        *
        * Se obtuvo:
        * active = true
        *
        * Revisar:
        * - eliminación lógica
        * - persistencia del cambio
        * - llamada a repository.save()
        */
    }
        @Test
    void deletePayment_DeberiaLanzarExcepcionCuandoPagoNoExiste() {

        // ARRANGE: preparar datos y mocks.

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y verificar excepción.

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> service.deletePayment(999L)
                );

        assertEquals(
                "Pago no encontrado con ID: 999",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock.

        verify(repository)
                .findById(999L);

        verify(repository, never())
                .save(any(Payment.class));

        /*
        * Caso hipotético de falla para QA:
        *
        * Se esperaba:
        * ResourceNotFoundException
        *
        * Se obtuvo:
        * Eliminación realizada o excepción incorrecta
        *
        * Revisar:
        * - validación de existencia
        * - uso de orElseThrow()
        * - flujo deletePayment()
        */
    }


}
