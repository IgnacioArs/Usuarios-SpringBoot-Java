package com.evaluacion.usuarios.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class UserRequestDtoTest {

    @Test
    void userRequestDto_allArgsConstructorAndGettersSetters_shouldWork() {
        PhoneRequestDto phone = new PhoneRequestDto(123456789L, 1, "56");
        UserRequestDto dto = new UserRequestDto();
        
        dto.setName("Juan Pérez");
        dto.setEmail("juan@example.com");
        dto.setPassword("A1b2c3d4");
        dto.setPhones(Arrays.asList(phone));

        assertEquals("Juan Pérez", dto.getName());
        assertEquals("juan@example.com", dto.getEmail());
        assertEquals("A1b2c3d4", dto.getPassword());
        assertNotNull(dto.getPhones());
        assertEquals(1, dto.getPhones().size());

        PhoneRequestDto p = dto.getPhones().get(0);
        assertEquals(123456789L, p.getNumber());
        assertEquals(1, p.getCitycode());
        assertEquals("56", p.getCountrycode());
    }

    @Test
    void userRequestDto_noArgsConstructor_shouldCreateEmptyObject() {
        UserRequestDto dto = new UserRequestDto();
        assertNull(dto.getName());
        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
        assertNull(dto.getPhones());
    }
}
