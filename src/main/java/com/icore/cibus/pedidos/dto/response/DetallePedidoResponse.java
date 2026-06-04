package com.icore.cibus.pedidos.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetallePedidoResponse {

    private final Long id;
    private final Long productoId;
    private final String productoNombre;
    private final Integer cantidad;
    private final BigDecimal precioUnitario;
    private final BigDecimal subtotal;
    private final String observaciones;
}
