package com.kartago.blacklotus.config;

import com.kartago.blacklotus.exceptions.AccesoDenegadoException;
import com.kartago.blacklotus.exceptions.ElementoNoEncontradoException;
import com.kartago.blacklotus.exceptions.ElementoYaExistenteException;
import com.kartago.blacklotus.exceptions.NumeroInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ElementoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> catchElementoNoEncontrado(ElementoNoEncontradoException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ElementoYaExistenteException.class)
    public ResponseEntity<Map<String, String>> catchElementoYaExistente(ElementoYaExistenteException ex) {

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(NumeroInvalidoException.class)
    public ResponseEntity<Map<String,String>> catchNumeroInvalido(NumeroInvalidoException ex){
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<Map<String,String>> catchAccesoNegado(AccesoDenegadoException ex){
        Map<String,String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }


    // Manejo genérico para cualquier otra excepción no controlada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> catchException(Exception ex) {
        Map<String, String> error = new HashMap<>();
//        error.put("error", "Ha ocurrido un error inesperado");
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
