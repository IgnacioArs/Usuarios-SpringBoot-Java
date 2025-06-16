package com.evaluacion.usuarios;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import com.evaluacion.usuarios.response.*;


class ErrorResponseTest {

    @Test
    void testConstructorAndGetters() {
        int code = 404;
        String detail = "Usuario no encontrado";

        ErrorResponse response = new ErrorResponse(code, detail);

        assertEquals(code, response.getCodigo());
        assertEquals(detail, response.getDetail());
        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
