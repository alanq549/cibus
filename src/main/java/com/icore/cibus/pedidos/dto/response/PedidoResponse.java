package com.icore.cibus.pedidos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.icore.cibus.shared.enums.EstadoPedido;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PedidoResponse {

    private final Long id;
    private final Long clienteId;
    private final String clienteNombre;
    private final Long empleadoId;
    private final LocalDateTime fechaPedido;
    private final BigDecimal total;
    private final EstadoPedido estado;
    private final String observaciones;
    private final List<DetallePedidoResponse> detalles;
}
