package com.evaluacion.usuarios;

import com.evaluacion.auth.service.AuthService;
import com.evaluacion.usuarios.dto.PhoneRequestDto;
import com.evaluacion.usuarios.dto.UserRequestDto;
import com.evaluacion.usuarios.model.Phone;
import com.evaluacion.usuarios.model.User;
import com.evaluacion.usuarios.repository.UsersRepository;
import com.evaluacion.usuarios.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSingUpMethod_successful() {
        // Arrange
        UserRequestDto dto = new UserRequestDto();
        dto.setName("Juan");
        dto.setEmail("juan@mail.com");
        dto.setPassword("password");

        PhoneRequestDto phone = new PhoneRequestDto();
        phone.setNumber(123456L);
        phone.setCitycode(1);
        phone.setCountrycode("56");
        dto.setPhones(List.of(phone));

        when(usersRepository.findByEmail("juan@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encrypted");
        when(jwtUtil.generateToken("juan@mail.com")).thenReturn("fake-jwt");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(usersRepository.save(userCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User user = authService.singUpMethod(dto);

        // Assert
        assertEquals("Juan", user.getName());
        assertEquals("juan@mail.com", user.getEmail());
        assertEquals("encrypted", user.getPassword());
        assertEquals("fake-jwt", user.getToken());
        assertNotNull(user.getCreated());
        assertNotNull(user.getPhones());
        assertEquals(1, user.getPhones().size());

        verify(usersRepository).save(any(User.class));
    }

    @Test
    void testSingUpMethod_userAlreadyExists() {
        UserRequestDto dto = new UserRequestDto();
        dto.setEmail("exists@mail.com");

        when(usersRepository.findByEmail("exists@mail.com")).thenReturn(Optional.of(new User()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.singUpMethod(dto);
        });

        assertEquals("Usuario ya registrado", exception.getMessage());
    }

    @Test
    void testLogin_successful() {
        String token = "valid-token";
        String email = "login@mail.com";

        User user = new User();
        user.setEmail(email);
        user.setLastLogin(new Date());

        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getEmailFromToken(token)).thenReturn(email);
        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(email)).thenReturn("new-token");

        User result = authService.login(token);

        assertEquals(email, result.getEmail());
        assertEquals("new-token", result.getToken());
    }

    @Test
    void testLogin_invalidToken() {
        String token = "invalid";

        when(jwtUtil.validateToken(token)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(token);
        });

        assertEquals("Token inválido o expirado", exception.getMessage());
    }

    @Test
    void testLogin_userNotFound() {
        String token = "valid";
        String email = "missing@mail.com";

        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getEmailFromToken(token)).thenReturn(email);
        when(usersRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(token);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }
}
