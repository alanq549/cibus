package com.icore.cibus.cocina.dto.response;

import java.time.LocalDateTime;

import com.icore.cibus.shared.enums.EstadoOrdenCocina;
import com.icore.cibus.shared.enums.EstadoPedido;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrdenCocinaResponse {

    private final Long id;
    private final EstadoOrdenCocina estado;
    private final LocalDateTime fechaCreacion;
    private final LocalDateTime fechaInicio;
    private final LocalDateTime fechaFinalizacion;
    private final String observaciones;
    private final Long pedidoId;
    private final EstadoPedido estadoPedido;
}
