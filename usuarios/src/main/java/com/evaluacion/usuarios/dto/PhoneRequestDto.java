package com.evaluacion.usuarios.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneRequestDto {
    private Long number;
    private Integer citycode;
    private String countrycode;
}
