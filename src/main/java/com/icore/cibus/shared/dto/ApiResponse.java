package com.icore.cibus.shared.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {

    private final boolean exito;
    private final String mensaje;
    private final T datos;
    @Builder.Default
    private final LocalDateTime fechaHora = LocalDateTime.now();
}
