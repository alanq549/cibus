package com.icore.cibus.inventario.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearInsumoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar 120 caracteres")
    private String nombre;

    @NotNull(message = "El stock actual es obligatorio")
    @DecimalMin(value = "0.000", message = "El stock actual no puede ser negativo")
    private BigDecimal stockActual;

    @NotNull(message = "El stock minimo es obligatorio")
    @DecimalMin(value = "0.000", message = "El stock minimo no puede ser negativo")
    private BigDecimal stockMinimo;

    @NotBlank(message = "La unidad de medida es obligatoria")
    @Size(max = 30, message = "La unidad de medida no puede superar 30 caracteres")
    private String unidadMedida;
}
