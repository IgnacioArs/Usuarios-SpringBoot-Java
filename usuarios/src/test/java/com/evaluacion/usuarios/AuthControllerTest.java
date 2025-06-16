package com.evaluacion.usuarios;

import com.evaluacion.usuarios.dto.UserRequestDto;
import com.evaluacion.usuarios.model.User;
import com.evaluacion.usuarios.model.Phone;
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

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDto userRequestDto;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto();
        userRequestDto.setName("Test User");
        userRequestDto.setEmail("auth-test@example.com");
        userRequestDto.setPassword("Abcdef12");
    }

    @Test
    @DisplayName("Debería crear un nuevo usuario con status 201")
    void testPostSignUp_shouldReturnCreatedUser() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("auth-test@example.com"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("Debería devolver conflicto (409) al intentar registrar el mismo usuario")
    void testPostSignUp_shouldReturnConflict() throws Exception {
        User user = new User(
                UUID.randomUUID(),
                userRequestDto.getName(),
                userRequestDto.getEmail(),
                userRequestDto.getPassword(),
                new Date(),
                new Date(),
                true,
                "mock-token",
                new ArrayList<>()
        );

        Phone phone = new Phone();
        phone.setNumber(123456789L);
        phone.setCitycode(2);
        phone.setCountrycode("56");
        phone.setUser(user);

        user.setPhones(List.of(phone));
        userRepository.save(user);

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.detail").value("Usuario ya registrado"))
                .andExpect(jsonPath("$.codigo").value(409));
    }

    @Test
    @DisplayName("Debería devolver unauthorized (401) con token inválido en login")
    void testLogin_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/login")
                        .header("Authorization", "Bearer invalidtoken")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    

}
