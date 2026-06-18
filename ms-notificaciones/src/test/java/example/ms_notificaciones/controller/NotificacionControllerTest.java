package example.ms_notificaciones.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

import java.util.List;


import example.ms_notificaciones.dto.NotificacionRequestDTO;
import example.ms_notificaciones.dto.NotificacionResponseDTO;
import example.ms_notificaciones.exception.NotificacionNotFoundException;
import example.ms_notificaciones.service.NotificacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService service;

    @Test
    void deberiaListarNotificaciones() throws Exception {

        // ARRANGE: preparar datos y mocks

        NotificacionResponseDTO notificacion =
                new NotificacionResponseDTO();

        when(service.listarNotificaciones())
                .thenReturn(List.of(notificacion));

        // ACT + ASSERT: ejecutar endpoint y validar respuesta

        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk());

        // VERIFY: comprobar interacción con el mock

        verify(service, times(1))
                .listarNotificaciones();

        /*
         CASO HIPOTÉTICO DE FALLA PARA QA

         Se esperaba:
         HTTP 200 OK

         Se obtuvo:
         HTTP 500 Internal Server Error

         Posible causa:
         El controller no está manejando correctamente
         una respuesta del servicio o existe un error
         en la configuración del endpoint.
        */
    }

            @Test
        void deberiaBuscarNotificacionPorId() throws Exception {

            // ARRANGE: preparar datos y mocks

            Long id = 1L;

            NotificacionResponseDTO notificacion =
                    new NotificacionResponseDTO();

            when(service.buscarPorId(id))
                    .thenReturn(notificacion);

            // ACT + ASSERT: ejecutar endpoint y validar respuesta

            mockMvc.perform(get("/api/notificaciones/{id}", id))
                    .andExpect(status().isOk());

            // VERIFY: comprobar interacción con el mock

            verify(service, times(1))
                    .buscarPorId(id);

            /*
            CASO HIPOTÉTICO DE FALLA PARA QA

            Se esperaba:
            HTTP 200 OK

            Se obtuvo:
            HTTP 404 Not Found

            Posible causa:
            El endpoint no está correctamente mapeado
            o el ID recibido no está llegando al controller.
            */
    }
        @Test
    void deberiaBuscarNotificacionesPorUsuario() throws Exception {

        // ARRANGE: preparar datos y mocks

        Long userId = 10L;

        NotificacionResponseDTO notificacion =
                new NotificacionResponseDTO();

        when(service.buscarPorUsuario(userId))
                .thenReturn(List.of(notificacion));

        // ACT + ASSERT: ejecutar endpoint y validar respuesta

        mockMvc.perform(
                get("/api/notificaciones/usuario/{userId}", userId))
                .andExpect(status().isOk());

        // VERIFY: comprobar interacción con el mock

        verify(service, times(1))
                .buscarPorUsuario(userId);

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        HTTP 200 OK

        Se obtuvo:
        HTTP 404 Not Found

        Posible causa:
        La ruta /usuario/{userId} no está correctamente
        mapeada o el PathVariable no coincide con el
        definido en el controller.
        */
    }
        @Test
    void deberiaCrearNotificacion() throws Exception {

        // ARRANGE: preparar datos y mocks

        NotificacionResponseDTO response =
                new NotificacionResponseDTO();

        when(service.crearNotificacion(
                any(NotificacionRequestDTO.class)))
                .thenReturn(response);

        String json = """
            {
            "userId": 1,
            "titulo": "Pedido enviado",
            "mensaje": "Tu pedido fue enviado correctamente",
            "tipo": "EMAIL"
            }
            """;

        // ACT + ASSERT: ejecutar endpoint y validar respuesta

        mockMvc.perform(post("/api/notificaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        // VERIFY: comprobar interacción con el mock

        verify(service, times(1))
                .crearNotificacion(any(NotificacionRequestDTO.class));

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        HTTP 201 Created

        Se obtuvo:
        HTTP 400 Bad Request

        Posible causa:
        El request no cumple las validaciones
        definidas en NotificacionRequestDTO.
        */
    }
        @Test
    void deberiaActualizarNotificacion() throws Exception {

        // ARRANGE: preparar datos y mocks

        Long id = 1L;

        NotificacionResponseDTO response =
                new NotificacionResponseDTO();

        when(service.actualizarNotificacion(
                eq(id),
                any(NotificacionRequestDTO.class)))
                .thenReturn(response);

        String json = """
            {
            "userId": 1,
            "titulo": "Pedido actualizado",
            "mensaje": "Tu pedido fue actualizado correctamente",
            "tipo": "EMAIL"
            }
            """;

        // ACT + ASSERT: ejecutar endpoint y validar respuesta

        mockMvc.perform(put("/api/notificaciones/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        // VERIFY: comprobar interacción con el mock

        verify(service, times(1))
                .actualizarNotificacion(
                        eq(id),
                        any(NotificacionRequestDTO.class));

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        HTTP 200 OK

        Se obtuvo:
        HTTP 400 Bad Request

        Posible causa:
        El DTO enviado incumple las validaciones
        definidas en NotificacionRequestDTO.
        */
    }
        @Test
    void deberiaEliminarNotificacion() throws Exception {

        // ARRANGE: preparar datos

        Long id = 1L;

        // ACT + ASSERT: ejecutar endpoint y validar respuesta

        mockMvc.perform(delete("/api/notificaciones/{id}", id))
                .andExpect(status().isNoContent());

        // VERIFY: comprobar interacción con el mock

        verify(service, times(1))
                .eliminarNotificacion(id);

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        HTTP 204 No Content

        Se obtuvo:
        HTTP 404 Not Found

        Posible causa:
        El ID no existe o la ruta DELETE
        está incorrectamente configurada.
        */
    }
        @Test
    void deberiaRetornar404CuandoNotificacionNoExiste() throws Exception {

        // ARRANGE: preparar datos y mocks

        Long id = 999L;

        when(service.buscarPorId(id))
                .thenThrow(
                        new NotificacionNotFoundException(
                                "Notificación no encontrada"));

        // ACT + ASSERT: ejecutar endpoint y validar respuesta

        mockMvc.perform(get("/api/notificaciones/{id}", id))
                .andExpect(status().isNotFound());

        // VERIFY: comprobar interacción con el mock

        verify(service, times(1))
                .buscarPorId(id);

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        HTTP 404 Not Found

        Se obtuvo:
        HTTP 500 Internal Server Error

        Posible causa:
        GlobalExceptionHandler no está capturando
        NotificacionNotFoundException.
        */
    }
        @Test
    void deberiaRetornar400CuandoRequestEsInvalido() throws Exception {

        // ARRANGE: JSON inválido

        String json = """
            {
            "userId": 0,
            "titulo": "",
            "mensaje": "",
            "tipo": ""
            }
            """;

        // ACT + ASSERT: ejecutar endpoint y validar respuesta

        mockMvc.perform(post("/api/notificaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        HTTP 400 Bad Request

        Se obtuvo:
        HTTP 201 Created

        Posible causa:
        Las validaciones del DTO no están siendo
        ejecutadas correctamente o falta @Valid.
        */
    }
        @Test
    void deberiaRetornar500CuandoOcurreErrorInterno() throws Exception {

        // ARRANGE: preparar datos y mocks

        Long id = 1L;

        when(service.buscarPorId(id))
                .thenThrow(new RuntimeException(
                        "Error inesperado"));

        // ACT + ASSERT: ejecutar endpoint y validar respuesta

        mockMvc.perform(get("/api/notificaciones/{id}", id))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message")
                .value("Error interno del servidor"));

        // VERIFY: comprobar interacción con el mock

        verify(service, times(1))
                .buscarPorId(id);

        /*
        CASO HIPOTÉTICO DE FALLA PARA QA

        Se esperaba:
        HTTP 500 Internal Server Error

        Se obtuvo:
        HTTP 404 Not Found

        Posible causa:
        La excepción está siendo interceptada
        por otro handler o no llega al
        GlobalExceptionHandler.
        */
    }
    
    
}
