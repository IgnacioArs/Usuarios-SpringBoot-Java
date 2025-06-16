package com.evaluacion.usuarios;

import com.evaluacion.usuarios.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.evaluacion.usuarios.exception.GlobalExceptionHandler;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleIllegalArgument_shouldReturnBadRequest() {
        String mensaje = "Argumento inválido";
        IllegalArgumentException exception = new IllegalArgumentException(mensaje);

        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgument(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(mensaje, response.getBody().getDetail());
        assertEquals(400, response.getBody().getCodigo());
    }

    @Test
    void handleException_shouldReturnInternalServerError() {
        Exception exception = new RuntimeException("Error");

        ResponseEntity<ErrorResponse> response = handler.handleException(exception);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error inesperado", response.getBody().getDetail());
        assertEquals(500, response.getBody().getCodigo());
    }

    @Test
    void handleConstraintViolationException_shouldReturnBadRequestWithMessages() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("email");

        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("no debe estar vacío");

        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

        ResponseEntity<?> response = handler.handleConstraintViolationException(exception);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof java.util.List);
        var errors = (java.util.List<?>) response.getBody();
        assertFalse(errors.isEmpty());
        assertEquals("email: no debe estar vacío", errors.get(0));
    }
}
