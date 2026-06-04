package com.icore.cibus.productos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.icore.cibus.productos.entity.EstadoProducto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductoResponse {

    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final BigDecimal precio;
    private final Boolean disponible;
    private final EstadoProducto estado;
    private final LocalDateTime creadoEn;
    private final LocalDateTime actualizadoEn;
}
