package com.evaluacion.usuarios.dto;


import lombok.*;
import javax.validation.constraints.*;
import java.util.List;
import com.evaluacion.usuarios.dto.PhoneRequestDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
 
     @NotBlank
    private String name;

    @Email(message = "El correo electrónico no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=(?:[^A-Z]*[A-Z]){1}[^A-Z]*$)(?=(?:[^\\d]*\\d){2}[^\\d]*$)[a-zA-Z\\d]{8,12}$",
        message = "La contraseña debe tener exactamente una mayúscula, dos números, letras minúsculas, sin caracteres especiales, y un largo entre 8 y 12 caracteres."
    )
    private String password;

    private List<PhoneRequestDto> phones;

    
}
