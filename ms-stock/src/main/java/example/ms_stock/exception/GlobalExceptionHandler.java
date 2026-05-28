package example.ms_stock.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Object>
    handleStockNotFound(
            StockNotFoundException ex) {

        log.warn("Stock no encontrado: {}",
                ex.getMessage());

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
    }

    @ExceptionHandler(StockAlreadyExistsException.class)
    public ResponseEntity<Object>
    handleStockAlreadyExists(
            StockAlreadyExistsException ex) {

        log.warn("Stock duplicado: {}",
                ex.getMessage());

        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Object>
    handleInsufficientStock(
            InsufficientStockException ex) {

        log.warn("Stock insuficiente: {}",
                ex.getMessage());

        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object>
    handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errores.put(
                                error.getField(),
                                error.getDefaultMessage()));

        Map<String, Object> body =
                new HashMap<>();

        body.put("timestamp",
                LocalDateTime.now());

        body.put("status",
                HttpStatus.BAD_REQUEST.value());

        body.put("errors",
                errores);

        return new ResponseEntity<>(
                body,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object>
    handleGeneral(Exception ex) {

        log.error("Error interno en ms-stock",
                ex);

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor");
    }

    private ResponseEntity<Object>
    buildResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> body =
                new HashMap<>();

        body.put("timestamp",
                LocalDateTime.now());

        body.put("status",
                status.value());

        body.put("message",
                message);

        return new ResponseEntity<>(
                body,
                status);
    }
}