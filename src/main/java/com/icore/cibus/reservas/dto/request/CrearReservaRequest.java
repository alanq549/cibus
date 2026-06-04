package com.icore.cibus.reservas.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearReservaRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "La mesa es obligatoria")
    private Long mesaId;

    @NotNull(message = "La fecha de reserva es obligatoria")
    @FutureOrPresent(message = "La fecha de reserva no puede estar en el pasado")
    private LocalDate fechaReserva;

    @NotNull(message = "La hora de reserva es obligatoria")
    private LocalTime horaReserva;

    @NotNull(message = "El numero de personas es obligatorio")
    @Min(value = 1, message = "El numero de personas debe ser mayor a cero")
    private Integer numeroPersonas;
}
