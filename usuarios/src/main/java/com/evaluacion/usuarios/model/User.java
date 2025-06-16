package com.evaluacion.usuarios.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.GenericGenerator;
import java.util.*;
import com.evaluacion.usuarios.model.Phone;
import com.fasterxml.jackson.annotation.JsonManagedReference;



@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class User {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID(); // clave primaria

    private String name;

    @Email(message= "El correo electronico no es valido")
    @Column(unique=true)
    private String email;

    @NotNull(message = "La contraseña no puede ser nula")
    @Column(nullable = false)
    private String password;

    //asignamos las fechas
    private Date created = new Date();
    private Date lastLogin;
    private boolean isActive = true;

    private String token;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Phone> phones;


}
