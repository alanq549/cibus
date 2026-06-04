package com.icore.cibus.entregas.dto.request;

import com.icore.cibus.shared.enums.EstadoEntrega;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoEntregaRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoEntrega estado;
}
