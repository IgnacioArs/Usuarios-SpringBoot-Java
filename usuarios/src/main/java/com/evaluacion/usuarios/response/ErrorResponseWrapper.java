package com.evaluacion.usuarios.response;

import java.util.List;

public class ErrorResponseWrapper {

    private List<ErrorResponse> error;

    public ErrorResponseWrapper(List<ErrorResponse> error) {
        this.error = error;
    }

    public List<ErrorResponse> getError() {
        return error;
    }

    public void setError(List<ErrorResponse> error) {
        this.error = error;
    }
}
