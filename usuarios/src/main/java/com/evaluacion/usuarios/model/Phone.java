package com.evaluacion.usuarios.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;
import com.evaluacion.usuarios.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="phones")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(exclude = "user")
public class Phone {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id; // ID telefono

   private Long number;
   private Integer citycode;
   private String countrycode;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "user_id", nullable = false) //nombre de la columna 
   @JsonBackReference  // evita loops infinitos de referencia
   private  User user;

}