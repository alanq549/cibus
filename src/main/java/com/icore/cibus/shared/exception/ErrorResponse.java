package com.icore.cibus.shared.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private final int estado;
    private final String error;
    private final String mensaje;
    private final String ruta;
    private final Map<String, String> validaciones;
    @Builder.Default
    private final LocalDateTime fechaHora = LocalDateTime.now();
}
