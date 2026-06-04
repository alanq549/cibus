package com.icore.cibus.pagos.dto.request;

import java.math.BigDecimal;

import com.icore.cibus.shared.enums.EstadoPago;
import com.icore.cibus.shared.enums.MetodoPago;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearPagoRequest {

    @NotNull(message = "El pedido es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El empleado es obligatorio")
    private Long empleadoId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    @NotNull(message = "El metodo de pago es obligatorio")
    private MetodoPago metodoPago;

    @NotNull(message = "El estado del pago es obligatorio")
    private EstadoPago estado;
}
