package com.icore.cibus.entregas.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearEntregaRequest {

    @NotNull(message = "El pedido es obligatorio")
    private Long pedidoId;

    @NotNull(message = "El repartidor es obligatorio")
    private Long repartidorId;

    @NotBlank(message = "La direccion de entrega es obligatoria")
    @Size(max = 255, message = "La direccion de entrega no puede superar 255 caracteres")
    private String direccionEntrega;
}
