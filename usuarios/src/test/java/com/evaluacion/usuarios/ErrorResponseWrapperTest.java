package com.evaluacion.usuarios;

import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import com.evaluacion.usuarios.response.*;

class ErrorResponseWrapperTest {

    @Test
    void testConstructorAndGetters() {
        ErrorResponse error = new ErrorResponse(400, "Solicitud inválida");
        ErrorResponseWrapper wrapper = new ErrorResponseWrapper(List.of(error));

        assertNotNull(wrapper.getError());
        assertEquals(1, wrapper.getError().size());
        assertEquals(400, wrapper.getError().get(0).getCodigo());
    }

    @Test
    void testSetters() {
        ErrorResponse error1 = new ErrorResponse(401, "No autorizado");
        ErrorResponse error2 = new ErrorResponse(403, "Prohibido");

        ErrorResponseWrapper wrapper = new ErrorResponseWrapper(List.of(error1));
        wrapper.setError(List.of(error2));

        assertEquals(1, wrapper.getError().size());
        assertEquals(403, wrapper.getError().get(0).getCodigo());
    }
}
