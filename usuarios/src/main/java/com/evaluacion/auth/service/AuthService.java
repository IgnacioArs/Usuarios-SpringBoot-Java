package com.evaluacion.auth.service;

import org.springframework.stereotype.Service;
import com.evaluacion.usuarios.dto.PhoneRequestDto;
import com.evaluacion.usuarios.dto.UserRequestDto;
import com.evaluacion.usuarios.model.Phone;
import com.evaluacion.usuarios.model.User;
import com.evaluacion.usuarios.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.evaluacion.usuarios.security.JwtUtil;
import java.util.*;

@Service
public class AuthService {
    
        @Autowired
        private UsersRepository usersRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private JwtUtil jwtUtil;


        
    public User singUpMethod(UserRequestDto userRequestDto) {
        if (usersRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Usuario ya registrado");
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


    public User login(String token) {
        System.out.println("Validando token: " + token);

        if (!jwtUtil.validateToken(token)) {
            System.out.println("Token inválido");
            throw new IllegalArgumentException("Token inválido o expirado");
        }

        String email = jwtUtil.getEmailFromToken(token);
        System.out.println("Email desde token: " + email);

        Optional<User> userOpt = usersRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            System.out.println("Usuario no encontrado");
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        User user = userOpt.get();

        System.out.println("Usuario encontrado: " + user.getEmail());

        user.setLastLogin(new Date());
        user.setToken(jwtUtil.generateToken(email));
        usersRepository.save(user);

        return user;
    }




}
