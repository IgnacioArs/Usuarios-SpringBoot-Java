package com.evaluacion.usuarios.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneRequestDtoTest {

    @Test
    void phoneRequestDto_allArgsConstructorAndGettersSetters_shouldWork() {
        PhoneRequestDto dto = new PhoneRequestDto(987654321L, 2, "34");

        assertEquals(987654321L, dto.getNumber());
        assertEquals(2, dto.getCitycode());
        assertEquals("34", dto.getCountrycode());

        dto.setNumber(55555555L);
        dto.setCitycode(3);
        dto.setCountrycode("51");

        assertEquals(55555555L, dto.getNumber());
        assertEquals(3, dto.getCitycode());
        assertEquals("51", dto.getCountrycode());
    }

    @Test
    void phoneRequestDto_noArgsConstructor_shouldCreateEmptyObject() {
        PhoneRequestDto dto = new PhoneRequestDto();
        assertNull(dto.getNumber());
        assertNull(dto.getCitycode());
        assertNull(dto.getCountrycode());
    }
}
