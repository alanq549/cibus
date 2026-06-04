package com.icore.cibus.shared.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException exception,
            HttpServletRequest request
    ) {
        return construirRespuesta(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoDuplicado(
            RecursoDuplicadoException exception,
            HttpServletRequest request
    ) {
        return construirRespuesta(HttpStatus.CONFLICT, exception.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidaciones(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> validaciones = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> validaciones.put(error.getField(), error.getDefaultMessage()));

        return construirRespuesta(
                HttpStatus.BAD_REQUEST,
                "La peticion contiene datos invalidos",
                request.getRequestURI(),
                validaciones
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorGeneral(Exception exception, HttpServletRequest request) {
        return construirRespuesta(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrio un error interno",
                request.getRequestURI(),
                null
        );
    }

    private ResponseEntity<ErrorResponse> construirRespuesta(
            HttpStatus estado,
            String mensaje,
            String ruta,
            Map<String, String> validaciones
    ) {
        ErrorResponse response = ErrorResponse.builder()
                .estado(estado.value())
                .error(estado.getReasonPhrase())
                .mensaje(mensaje)
                .ruta(ruta)
                .validaciones(validaciones)
                .build();

        return ResponseEntity.status(estado).body(response);
    }
}
