package com.bank.movement.infrastructure.input.adapter.rest.error;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.ResponseEntity;
import com.bank.movement.infrastructure.exception.CustomException;
import com.bank.movement.infrastructure.exception.ErrorResponse;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleMovementException(CustomException ex) {
        ErrorResponse error = new ErrorResponse(ex.getStatus().value(), ex.getMessage());
        return Mono.just(ResponseEntity.status(ex.getStatus()).body(error));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(WebExchangeBindException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed: " + message);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed: " + message);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneric(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocurrió un error inesperado");
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }
}
