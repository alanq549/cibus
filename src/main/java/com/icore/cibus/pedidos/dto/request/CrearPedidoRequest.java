package com.icore.cibus.pedidos.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearPedidoRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    private Long empleadoId;

    @Size(max = 500, message = "Las observaciones no pueden superar 500 caracteres")
    private String observaciones;

    @Valid
    @NotEmpty(message = "El pedido debe tener al menos un detalle")
    private List<CrearDetallePedidoRequest> detalles;
}
