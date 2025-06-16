package com.evaluacion.usuarios;

import com.evaluacion.usuarios.dto.PhoneRequestDto;
import com.evaluacion.usuarios.dto.UserRequestDto;
import com.evaluacion.usuarios.model.Phone;
import com.evaluacion.usuarios.model.User;
import com.evaluacion.usuarios.repository.UsersRepository;
import com.evaluacion.usuarios.security.JwtUtil;
import com.evaluacion.usuarios.service.UsersService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsersService usersService;

    private UUID id;
    private User user;
    private UserRequestDto userDto;

    @BeforeEach
    void setup() {
        id = UUID.randomUUID();
        user = new User();
        user.setId(id);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPhones(new ArrayList<>()); // ✅ Solución agregada aquí

        PhoneRequestDto phoneDto = new PhoneRequestDto();
        phoneDto.setNumber(123456L);
        phoneDto.setCitycode(1);
        phoneDto.setCountrycode("56");

        userDto = new UserRequestDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setPhones(List.of(phoneDto));
    }

    @Test
    void getAllUsers_shouldReturnList() {
        when(usersRepository.findAll()).thenReturn(List.of(user));
        List<User> result = usersService.getAllUsers();
        assertEquals(1, result.size());
    }

    @Test
    void getUsersById_shouldReturnUser() {
        when(usersRepository.findById(id)).thenReturn(Optional.of(user));
        Optional<User> result = usersService.getUsersById(id);
        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
    }

    @Test
    void getUsersById_notFound_throwsException() {
        when(usersRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> usersService.getUsersById(id));
    }

    @Test
    void createUser_shouldReturnSavedUser() {
        when(usersRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(userDto.getEmail())).thenReturn("fake-token");
        when(usersRepository.save(any(User.class))).thenReturn(user);

        User result = usersService.createUser(userDto);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void createUser_existingEmail_throwsException() {
        when(usersRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        assertThrows(IllegalArgumentException.class, () -> usersService.createUser(userDto));
    }

    @Test
    void updateUser_shouldUpdateAndReturnUser() {
        when(usersRepository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(usersRepository.save(any(User.class))).thenReturn(user);

        User result = usersService.updateUser(id, userDto);

        assertNotNull(result);
        verify(usersRepository).save(any(User.class));
    }

    @Test
    void updateUser_notFound_throwsException() {
        when(usersRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> usersService.updateUser(id, userDto));
    }

    @Test
    void deleteUser_shouldDeleteAndReturnUser() {
        when(usersRepository.findById(id)).thenReturn(Optional.of(user));
        doNothing().when(usersRepository).deleteById(id);

        User result = usersService.deleteUser(id);

        assertNotNull(result);
        verify(usersRepository).deleteById(id);
    }

    @Test
    void deleteUser_notFound_throwsException() {
        when(usersRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> usersService.deleteUser(id));
    }
}
