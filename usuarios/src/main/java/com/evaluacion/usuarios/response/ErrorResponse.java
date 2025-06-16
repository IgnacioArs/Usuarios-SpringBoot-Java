package com.evaluacion.usuarios.response;

import java.time.LocalDateTime;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private int codigo;
    private String detail;

    public ErrorResponse(int codigo, String detail) {
        this.timestamp = LocalDateTime.now();
        this.codigo = codigo;
        this.detail = detail;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDetail() {
        return detail;
    }
}
