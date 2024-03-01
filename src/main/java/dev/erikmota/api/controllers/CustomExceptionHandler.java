package dev.erikmota.api.controllers;

import dev.erikmota.api.service.exceptions.JasperException;
import dev.erikmota.api.service.exceptions.PoiException;
import dev.erikmota.api.service.exceptions.ValidacaoException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.Objects;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<String> handleValidacaoException(ValidacaoException ex) {
        if (ex.getMessage().contains("não foi encontrado")){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PoiException.class)
    public ResponseEntity<String> handlePoiException(PoiException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(JasperException.class)
    public ResponseEntity<String> handleJasperException(JasperException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        String errorMessage = ex.toString();
        if (errorMessage.contains("NoResourceFoundException")){
            ex.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().body("Ocorreu um erro ao realizar a requisição!");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationError(MethodArgumentNotValidException e) {
        String errorMessage = "Erro: " + Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = ex.toString();
        if (errorMessage.contains("DateTimeParseException")){
            return ResponseEntity.badRequest().body("Data inválida!");
        }
        if (errorMessage.contains("'true' or 'false'")){
            return ResponseEntity.badRequest().body("Status inválido!");
        }
        if (errorMessage.contains("dev.erikmota.api.models.TipoPessoa")){
            return ResponseEntity.badRequest().body("Tipo da Pessoa inválido!");
        }
        return handleException(new Exception(ex));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex){
        String errorMessage = ex.toString();
        if (errorMessage.contains("Required request parameter 'monitoradorId'")){
            return ResponseEntity.badRequest().body("O monitorador é obrigatório!");
        }
        return handleException(new Exception(ex));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<String> handleMissingServletRequestPartException(MissingServletRequestPartException ex){
        String errorMessage = ex.toString();
        if (errorMessage.contains("Required part 'file' is not present")){
            return ResponseEntity.badRequest().body("É necessário inserir o arquivo para realizar a importação!");
        }
        return handleException(new Exception(ex));
    }
}