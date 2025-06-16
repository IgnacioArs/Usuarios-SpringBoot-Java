package com.evaluacion.usuarios;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import com.evaluacion.usuarios.model.User;
import com.evaluacion.usuarios.model.Phone;

class UserTest {

    @Test
    void testUserGettersAndSetters() {
        User user = new User();
        UUID id = UUID.randomUUID();
        user.setId(id);
        user.setName("Juan");
        user.setEmail("juan@mail.com");
        user.setPassword("Password123");
        user.setCreated(new Date());
        user.setLastLogin(new Date());
        user.setActive(true);
        user.setToken("token123");

        List<Phone> phones = new ArrayList<>();
        Phone phone = new Phone();
        phones.add(phone);
        user.setPhones(phones);

        assertEquals(id, user.getId());
        assertEquals("Juan", user.getName());
        assertEquals("juan@mail.com", user.getEmail());
        assertEquals("Password123", user.getPassword());
        assertNotNull(user.getCreated());
        assertNotNull(user.getLastLogin());
        assertTrue(user.isActive());
        assertEquals("token123", user.getToken());
        assertEquals(phones, user.getPhones());
    }

    @Test
    void testUserConstructorAllArgs() {
        UUID id = UUID.randomUUID();
        Date now = new Date();
        List<Phone> phones = new ArrayList<>();
        phones.add(new Phone());

        User user = new User(id, "Pedro", "pedro@mail.com", "Clave123", now, now, true, "jwt123", phones);

        assertEquals(id, user.getId());
        assertEquals("Pedro", user.getName());
        assertEquals("pedro@mail.com", user.getEmail());
        assertEquals("Clave123", user.getPassword());
        assertEquals(now, user.getCreated());
        assertEquals(now, user.getLastLogin());
        assertTrue(user.isActive());
        assertEquals("jwt123", user.getToken());
        assertEquals(phones, user.getPhones());
    }
}
