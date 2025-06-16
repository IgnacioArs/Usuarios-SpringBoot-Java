package com.evaluacion.auth.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.*;
import com.evaluacion.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;

import com.evaluacion.usuarios.dto.UserRequestDto;
import com.evaluacion.usuarios.model.User;

import org.springframework.http.*;
import com.evaluacion.usuarios.response.ErrorResponse;

@RestController
@RequestMapping("/")
public class AuthController {

    
    @Autowired
    private AuthService authService;

    // ---> lo de la prueba   /sign-up
    @Operation(summary = "sign-up")
    @PostMapping("/sign-up")
    public ResponseEntity<?> postSignUp(@RequestBody UserRequestDto userRequestDto) {
       
            try{
                User createdSingUp = authService.singUpMethod(userRequestDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdSingUp);
            } catch (IllegalArgumentException e) {
                ErrorResponse error = new ErrorResponse(409, e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            } catch (Exception e) {
                ErrorResponse error = new ErrorResponse(500,"INTERNAL_SERVER_ERROR" );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
    }


    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", ""); // quitar el prefijo
            User user = authService.login(jwt); // método en el service
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(401, "Token inválido o expirado"));
        }
    }


    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado: user no encontrado en request");
        }
        return ResponseEntity.ok(user);
    }
    
}