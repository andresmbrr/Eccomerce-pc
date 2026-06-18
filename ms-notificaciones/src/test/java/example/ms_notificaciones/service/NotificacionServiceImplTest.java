package  example.ms_notificaciones.service;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import example.ms_notificaciones.dto.NotificacionRequestDTO;
import example.ms_notificaciones.dto.NotificacionResponseDTO;
import example.ms_notificaciones.exception.NotificacionNotFoundException;
import example.ms_notificaciones.model.Notificacion;
import example.ms_notificaciones.repository.NotificacionRepository;
import example.ms_notificaciones.service.impl.NotificacionServiceImpl;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceImplTest {

    @Mock
    private NotificacionRepository repository;

    @InjectMocks
    private NotificacionServiceImpl service;

        @Test
    void deberiaListarSoloNotificacionesActivas() {

        // ARRANGE: preparar datos y mocks

        Notificacion activa = Notificacion.builder()
                .id(1L)
                .userId(10L)
                .titulo("Activa")
                .mensaje("Mensaje activo")
                .tipo("EMAIL")
                .enviado(true)
                .fechaEnvio(LocalDateTime.now())
                .active(true)
                .build();

        Notificacion inactiva = Notificacion.builder()
                .id(2L)
                .userId(20L)
                .titulo("Inactiva")
                .mensaje("Mensaje inactivo")
                .tipo("SMS")
                .enviado(true)
                .fechaEnvio(LocalDateTime.now())
                .active(false)
                .build();

        when(repository.findAll())
                .thenReturn(List.of(activa, inactiva));

        // ACT: ejecutar método

        List<NotificacionResponseDTO> resultado =
                service.listarNotificaciones();

        // ASSERT: verificar resultado esperado

        assertEquals(1, resultado.size());

        assertEquals(
                "Activa",
                resultado.get(0).getTitulo());

        // VERIFY: comprobar llamadas al mock

        verify(repository, times(1))
                .findAll();

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        1 notificación activa

        Se obtuvo:
        2 notificaciones

        Posible causa:
        El filtro de active=true no se está aplicando.
        */
    }
        @Test
    void deberiaBuscarNotificacionPorId() {

        // ARRANGE: preparar datos y mocks

        Long id = 1L;

        Notificacion notificacion = Notificacion.builder()
                .id(id)
                .userId(10L)
                .titulo("Pedido enviado")
                .mensaje("Tu pedido fue enviado")
                .tipo("EMAIL")
                .enviado(true)
                .fechaEnvio(LocalDateTime.now())
                .active(true)
                .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(notificacion));

        // ACT: ejecutar método

        NotificacionResponseDTO resultado =
                service.buscarPorId(id);

        // ASSERT: verificar resultado esperado

        assertNotNull(resultado);

        assertEquals(id, resultado.getId());

        assertEquals(
                "Pedido enviado",
                resultado.getTitulo());

        // VERIFY: comprobar llamadas al mock

        verify(repository, times(1))
                .findById(id);

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        Obtener la notificación solicitada

        Se obtuvo:
        Notificación incorrecta o null

        Posible causa:
        Error en el mapeo a DTO.
        */
    }
        @Test
    void deberiaLanzarExcepcionCuandoNotificacionNoExiste() {

        // ARRANGE: preparar datos y mocks

        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y validar excepción

        NotificacionNotFoundException exception =
                assertThrows(
                        NotificacionNotFoundException.class,
                        () -> service.buscarPorId(id)
                );

        assertEquals(
                "Notificación no encontrada con ID: 999",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock

        verify(repository, times(1))
                .findById(id);

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        NotificacionNotFoundException

        Se obtuvo:
        NullPointerException

        Posible causa:
        No se está controlando correctamente
        el Optional vacío.
        */
    }
            @Test
    void deberiaCrearNotificacion() {

        // ARRANGE: preparar datos y mocks

        NotificacionRequestDTO request =
                new NotificacionRequestDTO();

        request.setUserId(10L);
        request.setTitulo("Pedido enviado");
        request.setMensaje("Tu pedido fue enviado correctamente");
        request.setTipo("EMAIL");

        Notificacion entidadGuardada =
                Notificacion.builder()
                        .id(1L)
                        .userId(10L)
                        .titulo("Pedido enviado")
                        .mensaje("Tu pedido fue enviado correctamente")
                        .tipo("EMAIL")
                        .enviado(true)
                        .fechaEnvio(LocalDateTime.now())
                        .active(true)
                        .build();

        when(repository.save(any(Notificacion.class)))
                .thenReturn(entidadGuardada);

        // ACT: ejecutar método

        NotificacionResponseDTO resultado =
                service.crearNotificacion(request);

        // ASSERT: verificar resultado esperado

        assertNotNull(resultado);

        assertEquals(1L, resultado.getId());

        assertEquals(
                "Pedido enviado",
                resultado.getTitulo());

        assertEquals(
                "EMAIL",
                resultado.getTipo());

        assertEquals(
                10L,
                resultado.getUserId());

        // VERIFY: comprobar llamadas al mock

        verify(repository, times(1))
                .save(any(Notificacion.class));

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        DTO con los datos de la notificación creada

        Se obtuvo:
        Campos nulos o incorrectos

        Posible causa:
        Error en mapToDTO() o en el save().
        */
    }
        @Test
    void deberiaBuscarSoloNotificacionesActivasPorUsuario() {

        // ARRANGE: preparar datos y mocks

        Long userId = 10L;

        Notificacion activa = Notificacion.builder()
                .id(1L)
                .userId(userId)
                .titulo("Notificación activa")
                .mensaje("Mensaje activo")
                .tipo("EMAIL")
                .enviado(true)
                .fechaEnvio(LocalDateTime.now())
                .active(true)
                .build();

        Notificacion inactiva = Notificacion.builder()
                .id(2L)
                .userId(userId)
                .titulo("Notificación inactiva")
                .mensaje("Mensaje inactivo")
                .tipo("SMS")
                .enviado(true)
                .fechaEnvio(LocalDateTime.now())
                .active(false)
                .build();

        when(repository.findByUserId(userId))
                .thenReturn(List.of(activa, inactiva));

        // ACT: ejecutar método

        List<NotificacionResponseDTO> resultado =
                service.buscarPorUsuario(userId);

        // ASSERT: verificar resultado esperado

        assertEquals(1, resultado.size());

        assertEquals(
                "Notificación activa",
                resultado.get(0).getTitulo());

        assertEquals(
                userId,
                resultado.get(0).getUserId());

        // VERIFY: comprobar llamadas al mock

        verify(repository, times(1))
                .findByUserId(userId);

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        1 notificación activa

        Se obtuvo:
        2 notificaciones

        Posible causa:
        No se está aplicando el filtro active=true.
        */
    }
        @Test
    void deberiaActualizarNotificacion() {

        // ARRANGE: preparar datos y mocks

        Long id = 1L;

        Notificacion existente = Notificacion.builder()
                .id(id)
                .userId(10L)
                .titulo("Bienvenido")
                .mensaje("Mensaje original")
                .tipo("EMAIL")
                .enviado(true)
                .fechaEnvio(LocalDateTime.now())
                .active(true)
                .build();

        NotificacionRequestDTO request =
                new NotificacionRequestDTO();

        request.setUserId(20L);
        request.setTitulo("Pedido entregado");
        request.setMensaje("Tu pedido fue entregado");
        request.setTipo("SMS");

        Notificacion actualizada = Notificacion.builder()
                .id(id)
                .userId(20L)
                .titulo("Pedido entregado")
                .mensaje("Tu pedido fue entregado")
                .tipo("SMS")
                .enviado(true)
                .fechaEnvio(existente.getFechaEnvio())
                .active(true)
                .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(existente));

        when(repository.save(any(Notificacion.class)))
                .thenReturn(actualizada);

        // ACT: ejecutar método

        NotificacionResponseDTO resultado =
                service.actualizarNotificacion(id, request);

        // ASSERT: verificar resultado esperado

        assertNotNull(resultado);

        assertEquals(id, resultado.getId());

        assertEquals(
                "Pedido entregado",
                resultado.getTitulo());

        assertEquals(
                "SMS",
                resultado.getTipo());

        assertEquals(
                20L,
                resultado.getUserId());

        // VERIFY: comprobar llamadas al mock

        verify(repository, times(1))
                .findById(id);

        verify(repository, times(1))
                .save(any(Notificacion.class));

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        Datos actualizados correctamente

        Se obtuvo:
        Datos antiguos o sin cambios

        Posible causa:
        Los setters no se ejecutan antes del save().
        */
    }
        @Test
    void deberiaLanzarExcepcionAlActualizarNotificacionInexistente() {

        // ARRANGE: preparar datos y mocks

        Long id = 999L;

        NotificacionRequestDTO request =
                new NotificacionRequestDTO();

        request.setUserId(10L);
        request.setTitulo("Actualización");
        request.setMensaje("Mensaje actualizado");
        request.setTipo("EMAIL");

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y validar excepción

        NotificacionNotFoundException exception =
                assertThrows(
                        NotificacionNotFoundException.class,
                        () -> service.actualizarNotificacion(id, request)
                );

        assertEquals(
                "Notificación no encontrada con ID: 999",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock

        verify(repository, times(1))
                .findById(id);

        verify(repository, never())
                .save(any(Notificacion.class));

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        NotificacionNotFoundException

        Se obtuvo:
        Actualización exitosa o NullPointerException

        Posible causa:
        No se valida correctamente la existencia
        de la notificación antes de actualizar.
        */
    }
        @Test
    void deberiaEliminarNotificacionLogicamente() {

        // ARRANGE: preparar datos y mocks

        Long id = 1L;

        Notificacion notificacion = Notificacion.builder()
                .id(id)
                .userId(10L)
                .titulo("Notificación")
                .mensaje("Mensaje")
                .tipo("EMAIL")
                .enviado(true)
                .fechaEnvio(LocalDateTime.now())
                .active(true)
                .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(notificacion));

        when(repository.save(any(Notificacion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT: ejecutar método

        service.eliminarNotificacion(id);

        // ASSERT: verificar resultado esperado

        assertFalse(notificacion.getActive());

        // VERIFY: comprobar llamadas al mock

        verify(repository, times(1))
                .findById(id);

        verify(repository, times(1))
                .save(notificacion);

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        active = false

        Se obtuvo:
        active = true

        Posible causa:
        No se ejecutó la eliminación lógica.
        */
    }
        @Test
    void deberiaLanzarExcepcionAlEliminarNotificacionInexistente() {

        // ARRANGE: preparar datos y mocks

        Long id = 999L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        // ACT + ASSERT: ejecutar método y validar excepción

        NotificacionNotFoundException exception =
                assertThrows(
                        NotificacionNotFoundException.class,
                        () -> service.eliminarNotificacion(id)
                );

        assertEquals(
                "Notificación no encontrada con ID: 999",
                exception.getMessage());

        // VERIFY: comprobar llamadas al mock

        verify(repository, times(1))
                .findById(id);

        verify(repository, never())
                .save(any(Notificacion.class));

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        NotificacionNotFoundException

        Se obtuvo:
        Eliminación exitosa o NullPointerException

        Posible causa:
        No se valida la existencia de la notificación
        antes de intentar eliminarla.
        */
    }


}