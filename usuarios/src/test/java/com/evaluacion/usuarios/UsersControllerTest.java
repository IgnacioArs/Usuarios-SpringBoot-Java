package com.evaluacion.usuarios;

import com.evaluacion.usuarios.dto.UserRequestDto;
import com.evaluacion.usuarios.model.Phone;
import com.evaluacion.usuarios.model.User;
import com.evaluacion.usuarios.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsersRepository userRepository;

    private UUID existingUserId;

    private String basicAuthHeader;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Juan");
        user.setEmail("test2@example.com");
        user.setPassword("Abcdef12");
        user.setCreated(new Date());
        user.setActive(true);
        user.setToken("test-token");

        Phone phone = new Phone();
        phone.setNumber(123456789L);
        phone.setCitycode(2);
        phone.setCountrycode("56");
        phone.setUser(user);

        user.setPhones(List.of(phone));
        userRepository.save(user);
        existingUserId = user.getId();

        // Genera el header de Basic Auth con "test:test123"
        basicAuthHeader = "Basic " + Base64.getEncoder().encodeToString("test:test123".getBytes());
    }

    @Test
    @DisplayName("Debería retornar la lista de usuarios")
    void getAllUsers_shouldReturnList() throws Exception {
        mockMvc.perform(get("/users/getAllUsers")
                        .header("Authorization", basicAuthHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Debería retornar 204 si no hay usuarios")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void getAllUsers_shouldReturnNoContent() throws Exception {
        userRepository.deleteAll();

        mockMvc.perform(get("/users/getAllUsers")
                        .header("Authorization", basicAuthHeader))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/users/getUserById/{id}", existingUserId)
                        .header("Authorization", basicAuthHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test2@example.com"));
    }

    @Test
    void getUserById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/users/getUserById/{id}", UUID.randomUUID())
                        .header("Authorization", basicAuthHeader))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Usuario no encontrado"));
    }

    @Test
    void createUser_shouldReturnCreated() throws Exception {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("Ana");
        dto.setEmail("test3@example.com");
        dto.setPassword("Abcdef12");

        mockMvc.perform(post("/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("Authorization", basicAuthHeader))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test3@example.com"));
    }

    @Test
    void createUser_shouldReturnConflict() throws Exception {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("Juan");
        dto.setEmail("test2@example.com"); // Email ya usado
        dto.setPassword("Abcdef12");

        mockMvc.perform(post("/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("Authorization", basicAuthHeader))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Email duplicado"));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("Juan Actualizado");
        dto.setEmail("juan.updated@example.com");
        dto.setPassword("Abcdef12");

        mockMvc.perform(put("/users/updateUser/{id}", existingUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("Authorization", basicAuthHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan.updated@example.com"));
    }

    @Test
    void updateUser_shouldReturnNotFound() throws Exception {
        UserRequestDto dto = new UserRequestDto();
        dto.setName("No Existe");
        dto.setEmail("noexiste@example.com");
        dto.setPassword("Abcdef12");

        mockMvc.perform(put("/users/updateUser/{id}", UUID.randomUUID())  // ID random que seguro no existe
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("Authorization", basicAuthHeader))
                .andExpect(status().isNotFound())  // Esperamos un 404 Not Found
                .andExpect(jsonPath("$.detail").value("Usuario no encontrado"));  // Y que el mensaje sea ese
    }


    @Test
    void deleteUser_shouldReturnDeletedUser() throws Exception {
        mockMvc.perform(delete("/users/deleteUser/{id}", existingUserId)
                        .header("Authorization", basicAuthHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test2@example.com"));
    }

    @Test
    void deleteUser_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/users/deleteUser/{id}", UUID.randomUUID())
                        .header("Authorization", basicAuthHeader))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Usuario no encontrado"));
    }
}
