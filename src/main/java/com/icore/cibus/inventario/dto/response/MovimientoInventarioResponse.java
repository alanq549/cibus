package com.icore.cibus.inventario.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.icore.cibus.shared.enums.TipoMovimientoInventario;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MovimientoInventarioResponse {

    private final Long id;
    private final TipoMovimientoInventario tipo;
    private final BigDecimal cantidad;
    private final String motivo;
    private final LocalDateTime fechaMovimiento;
    private final Long insumoId;
    private final String insumoNombre;
    private final Long empleadoId;
    private final BigDecimal stockActual;
}
