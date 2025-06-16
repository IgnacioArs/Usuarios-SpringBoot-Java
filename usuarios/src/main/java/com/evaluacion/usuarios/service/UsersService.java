package com.evaluacion.usuarios.service;

import com.evaluacion.usuarios.model.Phone;
import com.evaluacion.usuarios.model.User;
import com.evaluacion.usuarios.repository.UsersRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import com.evaluacion.usuarios.security.JwtUtil;
import com.evaluacion.usuarios.dto.PhoneRequestDto;
import com.evaluacion.usuarios.dto.UserRequestDto;

@Service
public class UsersService{

    @Autowired
    private UsersRepository usersRepository;

    @Autowired 
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    public List<User> getAllUsers(){
        return usersRepository.findAll();
    }


    public Optional<User> getUsersById(UUID id){
        Optional<User> usuarioEncontrado = usersRepository.findById(id);
        if(usuarioEncontrado.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        return usuarioEncontrado;
    }

    
    public User createUser(UserRequestDto userRequestDto) {
        if (usersRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email duplicado");
        }

        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setCreated(new Date());
        user.setLastLogin(new Date());
        user.setActive(true);
        user.setToken(jwtUtil.generateToken(userRequestDto.getEmail()));

        if (userRequestDto.getPhones() != null) {
            user.setPhones(new ArrayList<>());
            for (PhoneRequestDto phoneDto : userRequestDto.getPhones()) {
                Phone phone = new Phone();
                phone.setNumber(phoneDto.getNumber());
                phone.setCitycode(phoneDto.getCitycode());
                phone.setCountrycode(phoneDto.getCountrycode());
                phone.setUser(user);
                user.getPhones().add(phone);
            }
        }

        return usersRepository.save(user);
    }


    public User updateUser(UUID id, UserRequestDto updateUserRequest) {
        return usersRepository.findById(id).map(user -> {
            user.setName(updateUserRequest.getName());
            user.setEmail(updateUserRequest.getEmail());

            if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
            }

            user.setLastLogin(new Date());

            // Actualizar teléfonos
            if (updateUserRequest.getPhones() != null) {
                // Limpiar los teléfonos actuales
                user.getPhones().clear();

                for (PhoneRequestDto phoneDto : updateUserRequest.getPhones()) {
                    Phone phone = new Phone();
                    phone.setNumber(phoneDto.getNumber());
                    phone.setCitycode(phoneDto.getCitycode());
                    phone.setCountrycode(phoneDto.getCountrycode());
                    phone.setUser(user); // relación bidireccional
                    user.getPhones().add(phone);
                }
            }

            return usersRepository.save(user);
        }).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }



    public User deleteUser(UUID id) {
        Optional<User> userOpt = usersRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        User user = userOpt.get();
        usersRepository.deleteById(id);
        return user;
    }




}
