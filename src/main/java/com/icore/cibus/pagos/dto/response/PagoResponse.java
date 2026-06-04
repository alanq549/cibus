package com.icore.cibus.pagos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.icore.cibus.shared.enums.EstadoPago;
import com.icore.cibus.shared.enums.MetodoPago;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PagoResponse {

    private final Long id;
    private final Long pedidoId;
    private final Long empleadoId;
    private final BigDecimal monto;
    private final MetodoPago metodoPago;
    private final EstadoPago estado;
    private final LocalDateTime fechaPago;
}
