package com.bank.customer.domain.enums;

public enum ErrorMessage {
    CUSTOMER_NOT_FOUND("Cliente no encontrado"),
    INVALID_CUSTOMER_DATA("Datos del cliente no válidos"),
    CUSTOMER_ALREADY_EXISTS("El cliente ya existe"),
    CUSTOMER_UPDATE_FAILED("No se pudo actualizar el cliente"),
    UNEXPECTED_ERROR("Ocurrió un error inesperado");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}