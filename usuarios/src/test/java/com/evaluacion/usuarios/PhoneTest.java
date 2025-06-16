package com.evaluacion.usuarios;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.evaluacion.usuarios.model.User;
import com.evaluacion.usuarios.model.Phone;

class PhoneTest {

    @Test
    void testPhoneGettersAndSetters() {
        Phone phone = new Phone();
        phone.setId(1L);
        phone.setNumber(987654321L);
        phone.setCitycode(56);
        phone.setCountrycode("CL");

        User user = new User();
        phone.setUser(user);

        assertEquals(1L, phone.getId());
        assertEquals(987654321L, phone.getNumber());
        assertEquals(56, phone.getCitycode());
        assertEquals("CL", phone.getCountrycode());
        assertEquals(user, phone.getUser());
    }

    @Test
    void testPhoneConstructorAllArgs() {
        User user = new User();
        Phone phone = new Phone(2L, 123456789L, 1, "US", user);

        assertEquals(2L, phone.getId());
        assertEquals(123456789L, phone.getNumber());
        assertEquals(1, phone.getCitycode());
        assertEquals("US", phone.getCountrycode());
        assertEquals(user, phone.getUser());
    }
}
