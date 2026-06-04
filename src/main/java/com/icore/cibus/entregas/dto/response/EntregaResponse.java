package com.icore.cibus.entregas.dto.response;

import java.time.LocalDateTime;

import com.icore.cibus.shared.enums.EstadoEntrega;
import com.icore.cibus.shared.enums.EstadoPedido;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EntregaResponse {

    private final Long id;
    private final Long pedidoId;
    private final EstadoPedido estadoPedido;
    private final Long repartidorId;
    private final String direccionEntrega;
    private final EstadoEntrega estado;
    private final LocalDateTime fechaSalida;
    private final LocalDateTime fechaEntrega;
}
