package dev.erikmota.desafiounika.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = ex.getCause().getMessage();
        if (errorMessage.contains("DateTimeParseException")){
            return ResponseEntity.badRequest().body("Data inválida!");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Ocorreu um erro ao realizar a requisição!");
    }
}