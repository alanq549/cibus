package com.icore.cibus.cocina.dto.request;

import com.icore.cibus.shared.enums.EstadoOrdenCocina;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoOrdenCocinaRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoOrdenCocina estado;
}
