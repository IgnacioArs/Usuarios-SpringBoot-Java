package com.evaluacion.usuarios.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;

import com.evaluacion.usuarios.dto.UserRequestDto;
import com.evaluacion.usuarios.model.User;
import com.evaluacion.usuarios.service.UsersService;
import com.evaluacion.usuarios.response.ErrorResponse;



@RestController
@RequestMapping("/users")
public class UsersController{

      
        @Autowired
        private UsersService usersService;

        @Value("${jwt.secret}")
        private String secret;

        @Operation(summary = "Obtener todos los usuarios")
        @GetMapping("/getAllUsers")
        public ResponseEntity<List<User>> getAllUsers(){
            try{
                List<User> users = usersService.getAllUsers();

                if(users.isEmpty()){
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }else{
                    return new ResponseEntity<>(users, HttpStatus.OK); // 
                }

            }catch(Exception e){
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }   
        }

        @Operation(summary = "Obtener usuario por id")
        @GetMapping("/getUserById/{id}")
        public ResponseEntity<?> getUsersById(@PathVariable UUID id) {
            try {
                Optional<User> user = usersService.getUsersById(id);

                if (user.isPresent()) {
                    return ResponseEntity.ok(user.get());
                } else {
                    ErrorResponse error = new ErrorResponse(404, "Usuario no encontrado");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                }

            } catch (IllegalArgumentException e) {
                ErrorResponse error = new ErrorResponse(404, e.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            } catch (Exception e) {
                ErrorResponse error = new ErrorResponse(500, "INTERNAL_SERVER_ERROR");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        }

        @Operation(summary = "Crear Usuario")
        @PostMapping("/createUser")
        public ResponseEntity<?> createUser(@RequestBody UserRequestDto userRequestDto) {
            try {
                User createdUser = usersService.createUser(userRequestDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
            } catch (IllegalArgumentException e) {
                ErrorResponse error = new ErrorResponse(409,e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            } catch (Exception e) {
                ErrorResponse error = new ErrorResponse(500,"INTERNAL_SERVER_ERROR" );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
            }
        }

        @Operation(summary = "Actualizar Usuario por ID")
        @PutMapping("/updateUser/{id}")
        public ResponseEntity<?> updateUsersById(@PathVariable UUID id, @RequestBody UserRequestDto userRequestDto){
                
                try{
                    User updateUser = usersService.updateUser(id, userRequestDto);
                    return ResponseEntity.ok(updateUser);
                } catch (IllegalArgumentException e) {
                    ErrorResponse error = new ErrorResponse(404,e.getMessage());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                } catch (Exception e) {
                    ErrorResponse error = new ErrorResponse(500,"INTERNAL_SERVER_ERROR" );
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
                }
        }

        @Operation(summary = "Eliminar usuario por ID")
        @DeleteMapping("/deleteUser/{id}")
        public ResponseEntity<?> deleteUsersById(@PathVariable UUID id){
                
                try{

                    User usaurioEliminado = usersService.deleteUser(id);
                    return ResponseEntity.status(HttpStatus.OK).body(usaurioEliminado);
                
                } catch (IllegalArgumentException e) {

                    ErrorResponse error = new ErrorResponse(404,e.getMessage());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                
                } catch (Exception e) {
                
                    ErrorResponse error = new ErrorResponse(500,"INTERNAL_SERVER_ERROR" );
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
                
                }
        }
}
