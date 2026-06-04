package com.icore.cibus.productos.dto.response;

import java.math.BigDecimal;

import com.icore.cibus.shared.enums.EstadoProducto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductoResponse {

    private final Long id;
    private final String nombre;
    private final String descripcion;
    private final BigDecimal precio;
    private final EstadoProducto estado;
    private final Long categoriaId;
    private final String categoriaNombre;
}
