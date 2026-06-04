package com.icore.cibus.pedidos.dto.request;

import com.icore.cibus.shared.enums.EstadoPedido;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEstadoPedidoRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoPedido estado;
}
