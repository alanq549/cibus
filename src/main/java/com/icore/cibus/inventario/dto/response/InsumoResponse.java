package com.icore.cibus.inventario.dto.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InsumoResponse {

    private final Long id;
    private final String nombre;
    private final BigDecimal stockActual;
    private final BigDecimal stockMinimo;
    private final String unidadMedida;
    private final boolean stockBajo;
}
