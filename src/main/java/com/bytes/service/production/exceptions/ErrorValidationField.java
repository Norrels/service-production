package com.bytes.service.production.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorValidationField", description = "Representa a estrutura de um erro de validação de campo")
public class ErrorValidationField {
    @Schema(description = "Campo", example = "Nome")
    String field;

    @Schema(description = "Messagem de validação", example = "Nome não pode ser nulo")
    String message;

    public ErrorValidationField(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
};
