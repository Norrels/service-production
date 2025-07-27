package com.bytes.service.production.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa a estrutura de um erro no sistema")
public class ErrorMessageResponse {

    @Schema(description = "Mensagem de erro associada ao campo", example = "Valor inválido")
    String message;

    public ErrorMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
};
