package com.icore.cibus.inventario.dto.request;

import java.math.BigDecimal;

import com.icore.cibus.shared.enums.TipoMovimientoInventario;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarMovimientoInventarioRequest {

    @NotNull(message = "El insumo es obligatorio")
    private Long insumoId;

    @NotNull(message = "El empleado es obligatorio")
    private Long empleadoId;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimientoInventario tipo;

    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.000", message = "La cantidad no puede ser negativa")
    private BigDecimal cantidad;

    @Size(max = 255, message = "El motivo no puede superar 255 caracteres")
    private String motivo;
}
